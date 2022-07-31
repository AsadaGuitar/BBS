package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, ThreadId}
import com.github.asadaGuitar.bbs.repositories.MessagesRepository
import com.github.asadaGuitar.bbs.repositories.models.MessageForm
import com.github.asadaGuitar.bbs.usecases.models.PostMessageForm

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

final class MessageUseCase(messagesRepository: MessagesRepository)(implicit ec: ExecutionContext) {

  private def generateRandomMessageId(randomString: String = Random.alphanumeric.take(15).mkString): Future[MessageId] ={
    val messageId = MessageId(randomString)
    messagesRepository.existsById(messageId).flatMap{
      case true  => generateRandomMessageId()
      case false => Future.successful(messageId)
    }
  }

  def create(postMessageForm: PostMessageForm): Future[MessageId] ={
    postMessageForm match {
      case PostMessageForm(threadId, userId, text) =>
        for {
          messageId <- generateRandomMessageId()
          messageForm = MessageForm(messageId, threadId, userId, text)
          _ <- messagesRepository.save(messageForm)
        } yield messageId
    }
  }

  def findAllByThreadId(threadId: ThreadId): Future[List[Message]] =
    messagesRepository.findAllByThreadId(threadId)
}
