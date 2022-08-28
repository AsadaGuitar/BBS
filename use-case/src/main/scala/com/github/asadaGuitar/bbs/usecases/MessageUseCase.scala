package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{ Message, MessageId, MessageText, ThreadId, UserId }
import com.github.asadaGuitar.bbs.domains.repositories.MessagesRepository

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object MessageUseCase {
  final case class CreateCommand(threadId: ThreadId, userId: UserId, text: MessageText)
}

final class MessageUseCase(messagesRepository: MessagesRepository)(implicit ec: ExecutionContext) {
  import MessageUseCase._

  private[usecases] def generateRandomMessageId(length: Int): Future[MessageId] = {
    val messageId = MessageId(Random.alphanumeric.take(length).mkString)
    messagesRepository.existsById(messageId).flatMap {
      case true  => generateRandomMessageId(length)
      case false => Future.successful(messageId)
    }
  }

  def create(createCommand: CreateCommand): Future[MessageId] = {
    val CreateCommand(threadId, userId, text) = createCommand
    for {
      messageId <- generateRandomMessageId(12)
      message = Message(id = messageId, threadId = threadId, userId = userId, text = text)
      _ <- messagesRepository.save(message)
    } yield messageId
  }

  def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]] =
    messagesRepository
      .findAllByThreadId(threadId)
      .map(_.filter(!_.isClose))
}
