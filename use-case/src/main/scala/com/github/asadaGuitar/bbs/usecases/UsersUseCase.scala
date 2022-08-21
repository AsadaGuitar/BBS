package com.github.asadaGuitar.bbs.usecases

import cats.implicits.toTraverseOps
import com.github.asadaGuitar.bbs.domains.models.{EmailAddress, JwtToken, User, UserId, UserName, UserPassword}
import com.github.asadaGuitar.bbs.repositories.UsersRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object UsersUseCase {

  final case class SigninCommand(userId: UserId, password: UserPassword)

  final case class SignupCommand(emailAddress: EmailAddress,
                                  firstName: UserName,
                                  lastName: UserName,
                                  password: UserPassword)
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
      user = User(
        id = userId,
        firstName = firstName,
        lastName = lastName,
        emailAddress = emailAddress,
        password = password
      )
      _ <- userRepository.save(user)
    } yield userId
  }

  def signin(signinCommand: SigninCommand)(generateToken: UserId => JwtToken): Future[Option[JwtToken]] = {
    val SigninCommand(userId, password) = signinCommand
    for {
      userOption <- userRepository.findById(userId)
      isValidOption <- userOption.map { user =>
        Future.fromTry(user.password ?= password)
      }.sequence
    } yield {
      for {
        user <- userOption
        isValid <- isValidOption
        if isValid
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
