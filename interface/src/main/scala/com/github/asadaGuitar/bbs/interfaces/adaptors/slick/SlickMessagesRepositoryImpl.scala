package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.{models, _}
import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, MessageText, ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.MessagesRepository
import com.github.asadaGuitar.bbs.repositories.models.MessageForm

import java.sql.Date
import java.util
import scala.concurrent.{ExecutionContext, Future}

final class SlickMessagesRepositoryImpl(implicit ec: ExecutionContext) extends MessagesRepository with SlickDbConfigProvider {

  import dbConfig.profile.api._

  override def save(messageForm: MessageForm): Future[Int] = messageForm match {
    case MessageForm(id, threadId, userId, text) =>
      dbConfig.db.run {
        MessagesTable.table.insertOrUpdate {
          MessagesRecord(
            id = id.value,
            threadId = threadId.value,
            userId = userId.value,
            text = text.value
          )
        }
      }
  }

  override def findAllByThreadId(threadId: ThreadId): Future[List[Message]] =
    for {
      record <- dbConfig.db.run(MessagesTable.table.filter(_.threadId === threadId.value).result)
    } yield record.map {
      case MessagesRecord(id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) =>
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
    }.toList

  override def existsById(messageId: MessageId): Future[Boolean] =
    dbConfig.db.run(MessagesTable.table.filter(_.id === messageId.value).exists.result)


}

private[adaptors] case class MessagesRecord(id: String,
                                            threadId: String,
                                            userId: String,
                                            text: String,
                                            isClose: Boolean = false,
                                            createAt: Date = new Date(new util.Date().getTime),
                                            modifyAt: Option[Date] = None,
                                            closeAt: Option[Date] = None)

private[adaptors] object MessagesTable extends SlickDbConfigProvider {

  import dbConfig.profile.api._

  val table = TableQuery[MessagesTable]


  private[adaptors] class MessagesTable(tag: Tag) extends Table[MessagesRecord](tag, "messages") {
    def id = column[String]("id")

    def threadId = column[String]("thread_id")

    def userId = column[String]("user_id")

    def text = column[String]("text")

    def isClose = column[Boolean]("is_close")

    def createAt = column[java.sql.Date]("create_at")

    def modifyAt = column[Option[java.sql.Date]]("modify_at")

    def closeAt = column[Option[java.sql.Date]]("close_at")

    def messagesThreadId =
      foreignKey("messages_thread_id", threadId, ThreadsTable.table)(
        _.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction
      )

    def messagesUserId =
      foreignKey("messages_user_id", userId, UsersTable.table)(
        _.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction
      )

    override def * =
      (id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) <>
        ((MessagesRecord.apply _).tupled, MessagesRecord.unapply)
  }
}
