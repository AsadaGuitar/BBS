package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.asadaGuitar.bbs.domains.models.User
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.SlickUsersRepositoryImpl
import com.github.asadaGuitar.bbs.interfaces.controllers.models._
import com.github.asadaGuitar.bbs.interfaces.controllers.validations.ValidationDirectives
import com.github.asadaGuitar.bbs.usecases.UsersUseCase
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

final class UsersController(implicit val config: Config, executionContext: ExecutionContext)
    extends ValidationDirectives
    with JwtAuthenticator
    with Marshaller {

  private val logger = LoggerFactory.getLogger(getClass)

  private val usersRepository = new SlickUsersRepositoryImpl
  private val usersUseCase    = new UsersUseCase(usersRepository)

  val signupRouter: Route =
    pathPrefix("signup") {
      pathEndOrSingleSlash {
        post {
          entity(as[SignupRequestForm]) { signupRequestForm =>
            validateSignupRequestForm(signupRequestForm) { signupForm =>
              onComplete(usersUseCase.signup(signupForm)) {
                case Success(userId) =>
                  complete(SignupUserSucceededResponse(userId.value))
                case Failure(exception) =>
                  logger.error(s"A database error occurred during sign-up. ${exception.getMessage}")
                  throw exception
              }
            }
          }
        }
      }
    }

  val signinRouter: Route =
    pathPrefix("signin") {
      pathEndOrSingleSlash {
        post {
          entity(as[SigninRequestForm]) { signinRequestForm =>
            validateSigninRequestForm(signinRequestForm) { signinForm =>
              onComplete(usersUseCase.signin(signinForm)(this.generateToken)) {
                case Success(jwtToken) =>
                  jwtToken match {
                    case Some(token) => complete(SigninUserSucceededResponse(token.value))
                    case None        => complete(StatusCodes.BadRequest, ErrorResponse.signinFailure)
                  }
                case Failure(exception) =>
                  logger.error(s"A database error occurred during sign-in. ${exception.getMessage}")
                  throw exception
              }
            }
          }
        }
      }
    }

  val userAccountRouter: Route =
    pathPrefix("user_account" / Segment) { requestUserId =>
      validateUserId(requestUserId) { otherUserId =>
        authenticate { _ =>
          get {
            onComplete(usersUseCase.findById(otherUserId)) {
              case Success(user) =>
                user match {
                  case Some(user) =>
                    val User(id, firstName, lastName, emailAddress, _, isClose, _, _, _) = user
                    if (!isClose) {
                      complete(
                        FindUserByIdSucceededResponse(id.value, firstName.value, lastName.value, emailAddress.value)
                      )
                    } else {
                      complete(StatusCodes.NotFound, ErrorResponse.notFoundUser)
                    }
                  case None => complete(StatusCodes.NotFound, ErrorResponse.notFoundUser)
                }
              case Failure(exception) =>
                logger.error(s"A database error occurred during find user by id. ${exception.getMessage}")
                throw exception
            }
          }
        }
      }
    }
}
