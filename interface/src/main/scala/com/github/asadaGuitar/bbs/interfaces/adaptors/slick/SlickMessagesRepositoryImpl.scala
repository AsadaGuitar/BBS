package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.models
import com.github.asadaGuitar.bbs.domains.models.{ Message, MessageId, MessageText, ThreadId, UserId }
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import com.github.asadaGuitar.bbs.repositories.MessagesRepository

import scala.concurrent.{ ExecutionContext, Future }

final class SlickMessagesRepositoryImpl(implicit ec: ExecutionContext)
    extends MessagesRepository
    with SlickDbConfigProvider
    with DbColumnsConvertor {

  import dbConfig.profile.api._

  override def save(message: Message): Future[Int] = {
    val Message(id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) = message
    this.existsById(id).flatMap {
      case true =>
        dbConfig.db.run {
          Tables.Messages.insertOrUpdate {
            Tables.MessagesRow(
              id = id.value,
              threadId = threadId.value,
              userId = userId.value,
              text = text.value,
              isClose = isClose,
              createAt = createAt,
              modifyAt = modifyAt,
              closeAt = closeAt
            )
          }
        }
      case false =>
        Future.failed(new SlickException(s"message id already exists. ${message.id.value}"))
    }
  }

  override def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]] =
    for {
      rows <- dbConfig.db.run {
        Tables.Messages
          .filter(_.threadId === threadId.value)
          .result
      }
    } yield rows.map { row =>
      val Tables.MessagesRow(id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) = row
      models.Message(
        id = MessageId(id),
        threadId = ThreadId(threadId),
        userId = UserId(userId),
        text = MessageText(text),
        isClose = isClose,
        createAt = createAt,
        modifyAt = modifyAt,
        closeAt = closeAt
      )
    }.toVector

  override def existsById(messageId: MessageId): Future[Boolean] =
    dbConfig.db.run {
      Tables.Messages
        .filter(_.id === messageId.value)
        .exists
        .result
    }
}
