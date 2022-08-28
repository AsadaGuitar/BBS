package com.github.asadaGuitar.bbs.usecases

import cats.implicits.toTraverseOps
import com.github.asadaGuitar.bbs.domains.models.{BcryptedPassword, EmailAddress, Jwt, PlainPassword, User, UserId, UserName}
import com.github.asadaGuitar.bbs.domains.repositories.UsersRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object UsersUseCase {

  final case class SigninCommand(userId: UserId, plainPassword: PlainPassword)

  final case class SignupCommand(emailAddress: EmailAddress,
                                 firstName: UserName,
                                 lastName: UserName,
                                 plainPassword: PlainPassword)
}

final class UsersUseCase(userRepository: UsersRepository)(implicit ec: ExecutionContext) {

  import UsersUseCase._

  private[usecases] def generateRandomUserId(length: Int): Future[UserId] = {
    val userId = UserId(Random.alphanumeric.take(length).mkString)
    userRepository.existsById(userId).flatMap {
      case true => generateRandomUserId(length)
      case false => Future.successful(userId)
    }
  }

  def signup(signupCommand: SignupCommand): Future[UserId] = {
    val SignupCommand(emailAddress, firstName, lastName, password) = signupCommand
    for {
      _ <- userRepository.existsByEmailAddress(emailAddress).flatMap {
        case false => Future.unit
        case true =>
          Future.failed(
            new IllegalArgumentException(
              s"That email address is already in use. ${emailAddress.value}"))
      }
      userId <- generateRandomUserId(12)
      user <- Future.fromTry {
        password.bcryptSafeBounded.map { bcrypted =>
          User(
            id = userId,
            firstName = firstName,
            lastName = lastName,
            emailAddress = emailAddress,
            password = bcrypted
          )
        }
      }
      _ <- userRepository.save(user)
    } yield userId
  }

  def signin(signinCommand: SigninCommand)(generateToken: UserId => Jwt): Future[Option[Jwt]] = {
    val SigninCommand(userId, plainPassword) = signinCommand
    for {
      userOption <- userRepository.findById(userId)
      isMatchPasswordOption <- userOption.map { user =>
        user.password match {
          case bcryptedPassword@BcryptedPassword(_) =>
            Future.fromTry(bcryptedPassword.verify(plainPassword))
          case _ => Future.successful(false)
        }
      }.sequence
    } yield {
      for {
        user <- userOption
        isMatch <- isMatchPasswordOption
        if isMatch
      } yield {
        generateToken(user.id)
      }
    }
  }

  def findById(userId: UserId): Future[Option[User]] =
    userRepository.findById(userId).map {
      _.flatMap { user =>
        if (user.isClose) None else Some(user)
      }
    }
}
