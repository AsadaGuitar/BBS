package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, ThreadId}
import com.github.asadaGuitar.bbs.repositories.models.MessageForm

import scala.concurrent.Future

trait MessagesRepository {

  def save(messageForm: MessageForm): Future[Int]

  def findAllByThreadId(threadId: ThreadId): Future[List[Message]]

  def existsById(messageId: MessageId): Future[Boolean]
}
