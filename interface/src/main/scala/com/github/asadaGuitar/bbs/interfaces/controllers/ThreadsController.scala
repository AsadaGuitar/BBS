package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.asadaGuitar.bbs.domains.models.{ Message, Thread }
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.{
  SlickMessagesRepositoryImpl,
  SlickThreadsRepositoryImpl,
  SlickUserThreadsRepositoryImpl
}
import com.github.asadaGuitar.bbs.interfaces.controllers.models._
import com.github.asadaGuitar.bbs.interfaces.controllers.validations.ValidationDirectives
import com.github.asadaGuitar.bbs.usecases.{ MessageUseCase, ThreadUseCase }
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

final class ThreadsController(implicit val config: Config, executionContext: ExecutionContext)
    extends ValidationDirectives
    with JwtAuthenticator
    with Marshaller {

  private val logger = LoggerFactory.getLogger(getClass)

  private val userThreadsRepository = new SlickUserThreadsRepositoryImpl
  private val messageRepository     = new SlickMessagesRepositoryImpl
  private val threadsRepository     = new SlickThreadsRepositoryImpl

  private val threadUseCase  = new ThreadUseCase(threadsRepository, userThreadsRepository)
  private val messageUseCase = new MessageUseCase(messageRepository)

  val threadRouter: Route =
    pathPrefix("threads") {
      pathEndOrSingleSlash {
        authenticate { userId =>
          post {
            entity(as[PostThreadRequest]) { postThreadRequest =>
              validatePostThreadRequest(postThreadRequest) { postThreadRequestFormWithoutUserId =>
                val PostThreadFormWithoutUserId(title, otherUserIds) = postThreadRequestFormWithoutUserId
                val createCommand = ThreadUseCase.CreateCommand(userId, title, otherUserIds)
                onComplete(threadUseCase.create(createCommand)) {
                  case Success(threadId) =>
                    complete(PostThreadSucceededResponse(threadId.value))
                  case Failure(exception) =>
                    logger.error(s"A database error occurred during post thread. ${exception.getMessage}")
                    throw exception
                }
              }
            }
          } ~
          get {
            onComplete(threadUseCase.findAllByUserId(userId)) {
              case Success(threadList) =>
                val threadResponseList = threadList.map { case Thread(id, _, title, _, _, _, _) =>
                  FindThreadByUserIdSucceededResponse(id.value, title.value)
                }
                complete(threadResponseList)
              case Failure(exception) =>
                logger.error(
                  s"A database error occurred during find all threads by user id. ${exception.getMessage}"
                )
                throw exception
            }
          }
        }
      } ~ pathPrefix(Segment) { threadIdRequest =>
        validateThreadId(threadIdRequest) { threadId =>
          pathPrefix("messages") {
            pathEndOrSingleSlash {
              authenticate { userId =>
                post {
                  entity(as[PostMessageRequest]) { postMessageRequest =>
                    validatePostMessageRequest(postMessageRequest) { messageText =>
                      val createCommand = MessageUseCase.CreateCommand(threadId, userId, messageText)
                      onComplete(messageUseCase.create(createCommand)) {
                        case Success(messageId) =>
                          complete(PostMessageSucceededResponse(messageId.value))
                        case Failure(exception) =>
                          logger.error(s"A database error occurred during post message. ${exception.getMessage}")
                          throw exception
                      }
                    }
                  }
                } ~
                get {
                  onComplete(messageUseCase.findAllByThreadId(threadId)) {
                    case Success(messageList) =>
                      val responses = messageList.map { case Message(id, _, _, text, _, _, _, _) =>
                        FindMessageByThreadIdSucceededResponse(id.value, text.value)
                      }
                      complete(responses)
                    case Failure(exception) =>
                      logger.error(s"A database error occurred during post message. ${exception.getMessage}")
                      throw exception
                  }
                }
              }
            }
          }
        }
      }
    }
}
