package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{ JwtToken, User, UserId }
import com.github.asadaGuitar.bbs.repositories.UsersRepository
import com.github.asadaGuitar.bbs.repositories.models.UserForm
import com.github.asadaGuitar.bbs.usecases.models.{ SigninForm, SignupForm }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

final class UsersUseCase(userRepository: UsersRepository)(implicit ec: ExecutionContext) {

  private def generateRandomUserId(randomString: String = Random.alphanumeric.take(10).mkString): Future[UserId] = {
    val userId = UserId(randomString)
    userRepository.existsById(userId).flatMap {
      case true  => generateRandomUserId()
      case false => Future.successful(userId)
    }
  }

  def signup(signupForm: SignupForm): Future[UserId] =
    signupForm match {
      case SignupForm(emailAddress, firstName, lastName, password) =>
        for {
          userId <- generateRandomUserId()
          userForm = UserForm(userId, emailAddress, firstName, lastName, password)
          _ <- userRepository.save(userForm)
        } yield userId
    }

  def signin(signinForm: SigninForm)(generateToken: UserId => JwtToken): Future[Option[JwtToken]] =
    signinForm match {
      case SigninForm(userId, password) =>
        for {
          user <- userRepository.findById(userId)
        } yield {
          user.flatMap {
            case foundUser if foundUser.password.equals(password) =>
              Some(generateToken(userId))
            case _ => None
          }
        }
    }

  def findById(userId: UserId): Future[Option[User]] = userRepository.findById(userId)
}
