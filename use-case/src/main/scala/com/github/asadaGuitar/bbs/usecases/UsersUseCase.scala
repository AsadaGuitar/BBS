package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, JwtToken, User, UserId, UserName, UserPassword }
import com.github.asadaGuitar.bbs.repositories.UsersRepository

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object UsersUseCase {
  final case class SigninCommand(userId: UserId, password: UserPassword)
  final case class SignupCommand(
      emailAddress: EmailAddress,
      firstName: UserName,
      lastName: UserName,
      password: UserPassword
  )
}

final class UsersUseCase(userRepository: UsersRepository)(implicit ec: ExecutionContext) {
  import UsersUseCase._

  private def generateRandomUserId(randomString: String = Random.alphanumeric.take(10).mkString): Future[UserId] = {
    val userId = UserId(randomString)
    userRepository.existsById(userId).flatMap {
      case true  => generateRandomUserId()
      case false => Future.successful(userId)
    }
  }

  def signup(signupCommand: SignupCommand): Future[UserId] = {
    val SignupCommand(emailAddress, firstName, lastName, password) = signupCommand
    for {
      _ <- userRepository.existsByEmailAddress(emailAddress).flatMap {
        case true  => Future.unit
        case false => Future.failed(new IllegalArgumentException("That email address is already in use."))
      }
      userId <- generateRandomUserId()
      bcryptedPassword <- password.bcryptBoundedFuture
      user = User(
        id = userId,
        firstName = firstName,
        lastName = lastName,
        emailAddress = emailAddress,
        password = bcryptedPassword
      )
      _ <- userRepository.save(user)
    } yield userId
  }

  def signin(signinCommand: SigninCommand)(generateToken: UserId => JwtToken): Future[Option[JwtToken]] = {
    val SigninCommand(userId, password) = signinCommand
    for {
      user <- userRepository.findById(userId)
      bcryptedPassword <- password.bcryptBoundedFuture
    } yield {
      user.flatMap {
        case foundUser if foundUser.password.equals(bcryptedPassword) =>
          Some(generateToken(userId))
        case _ => None
      }
    }
  }

  def findById(userId: UserId): Future[Option[User]] =
    userRepository.findById(userId)
}
