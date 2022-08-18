package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{ Message, MessageId, MessageText, ThreadId, UserId }
import com.github.asadaGuitar.bbs.repositories.MessagesRepository

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object MessageUseCase {
  final case class CreateCommand(threadId: ThreadId, userId: UserId, text: MessageText)
}

final class MessageUseCase(messagesRepository: MessagesRepository)(implicit ec: ExecutionContext) {
  import MessageUseCase._

  private def generateRandomMessageId(
      randomString: String = Random.alphanumeric.take(15).mkString
  ): Future[MessageId] = {
    val messageId = MessageId(randomString)
    messagesRepository.existsById(messageId).flatMap {
      case true  => generateRandomMessageId()
      case false => Future.successful(messageId)
    }
  }

  def create(createCommand: CreateCommand): Future[MessageId] = {
    val CreateCommand(threadId, userId, text) = createCommand
    for {
      messageId <- generateRandomMessageId()
      message = Message(id = messageId, threadId = threadId, userId = userId, text = text)
      _ <- messagesRepository.save(message)
    } yield messageId
  }

  def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]] =
    messagesRepository
      .findAllByThreadId(threadId)
      .map(_.filter(!_.isClose))
}
