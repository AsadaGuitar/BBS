package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.{
  SlickMessagesRepositoryImpl,
  SlickThreadsRepositoryImpl,
  SlickUserThreadsRepositoryImpl
}
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{
  MessageResponse,
  PostMessageRequestForm,
  PostMessageSucceedResponse,
  PostThreadFormWithoutUserId,
  PostThreadRequestForm,
  PostThreadSucceededResponse,
  ThreadResponse
}
import com.github.asadaGuitar.bbs.interfaces.controllers.validations.ValidationDirectives
import com.github.asadaGuitar.bbs.domains.models.{ Message, Thread }
import com.github.asadaGuitar.bbs.usecases.models.{ PostMessageForm, PostThreadForm }
import com.github.asadaGuitar.bbs.usecases.{ MessageUseCase, ThreadUseCase }
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

final class ThreadsController(implicit system: ActorSystem[_])
    extends ValidationDirectives
    with JwtAuthenticator
    with Marshaller {

  implicit val ec: ExecutionContext    = system.executionContext
  override implicit val config: Config = system.settings.config

  private val userThreadsRepository = new SlickUserThreadsRepositoryImpl
  private val threadsRepository     = new SlickThreadsRepositoryImpl
  private val threadUseCase         = new ThreadUseCase(threadsRepository, userThreadsRepository)

  private val messageRepository = new SlickMessagesRepositoryImpl
  private val messageUseCase    = new MessageUseCase(messageRepository)

  val threadRouter: Route =
    pathPrefix("threads") {
      pathEndOrSingleSlash {
        authenticate { userId =>
          post {
            entity(as[PostThreadRequestForm]) { postThreadRequestForm =>
              validatePostThreadRequestForm(postThreadRequestForm) {
                case PostThreadFormWithoutUserId(title, otherUserIds) =>
                  val postThreadForm = PostThreadForm(userId, title, otherUserIds)
                  onComplete(threadUseCase.create(postThreadForm)) {
                    case Success(threadId) =>
                      complete(PostThreadSucceededResponse(threadId.value))
                    case Failure(exception) =>
                      system.log.error(s"A database error occurred during post thread. ${exception.getMessage}")
                      throw exception
                  }
              }
            }
          } ~
          get {
            onComplete(threadUseCase.findAllByUserId(userId)) {
              case Success(threadList) =>
                val threadResponseList = threadList.map { case Thread(id, _, title, _, _, _, _) =>
                  ThreadResponse(id.value, title.value)
                }
                complete(threadResponseList)
              case Failure(exception) =>
                system.log.error(
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
                  entity(as[PostMessageRequestForm]) { postMessageRequestForm =>
                    validatePostMessageRequestForm(postMessageRequestForm) { messageText =>
                      val postMessageForm = PostMessageForm(threadId, userId, messageText)
                      onComplete(messageUseCase.create(postMessageForm)) {
                        case Success(messageId) =>
                          complete(PostMessageSucceedResponse(messageId.value))
                        case Failure(exception) =>
                          system.log.error(s"A database error occurred during post message. ${exception.getMessage}")
                          throw exception
                      }
                    }
                  }
                } ~
                get {
                  onComplete(messageUseCase.findAllByThreadId(threadId)) {
                    case Success(messageList) =>
                      val messageResponseList = messageList.map { case Message(id, _, _, text, _, _, _, _) =>
                        MessageResponse(id.value, text.value)
                      }
                      complete(messageResponseList)
                    case Failure(exception) =>
                      system.log.error(s"A database error occurred during post message. ${exception.getMessage}")
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
