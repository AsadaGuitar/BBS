package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{ Message, MessageId, ThreadId }

import scala.concurrent.Future

trait MessagesRepository {

  def save(message: Message): Future[Int]

  def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]]

  def existsById(messageId: MessageId): Future[Boolean]
}
