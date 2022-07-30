package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.models.{ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.UserThreadsRepository
import com.github.asadaGuitar.bbs.repositories.models.UserThreadsForm

import java.util.{Date => UtilDate}
import java.sql.Date
import scala.concurrent.{ExecutionContext, Future}

final class SlickUserThreadsRepositoryImpl(implicit ec: ExecutionContext) extends UserThreadsRepository with SlickDbConfigProvider {

  import dbConfig.profile.api._

  override def save(userThreadsForm: UserThreadsForm): Future[Int] =
    userThreadsForm match {
      case UserThreadsForm(userId, threadId) =>
        dbConfig.db.run {
          UserThreadsTable.table.insertOrUpdate{
            UserThreadsRecord(userId = userId.value, threadId = threadId.value)
          }
        }
    }

  override def findAllByUserId(userId: UserId): Future[List[ThreadId]] =
    dbConfig.db.run {
      UserThreadsTable.table
        .filter(_.userId === userId.value)
        .map(_.threadId)
        .result
    }.map(_.toList.map(ThreadId))
}

private[adaptors] case class UserThreadsRecord(id: Int = 0,
                                               userId: String,
                                               threadId: String,
                                               isClose: Boolean = false,
                                               createAt: Date = {
                                                 val now = new UtilDate()
                                                 new Date(now.getTime)
                                               },
                                               closeAt: Option[Date] = None)

private[adaptors] object UserThreadsTable extends SlickDbConfigProvider {

  import dbConfig.profile.api._

  val table = TableQuery[UserThreadsTable]

  private[adaptors] class UserThreadsTable(tag: Tag) extends Table[UserThreadsRecord](tag, "user_threads") {

    def id = column[Int]("id")

    def userId = column[String]("user_id")

    def threadId = column[String]("thread_id")

    def isClose = column[Boolean]("is_close")

    def createAt = column[java.sql.Date]("create_at")

    def closeAt = column[Option[java.sql.Date]]("close_at")

    def userThreadsUserId =
      foreignKey("user_threads_user_id", userId, UsersTable.table)(
        _.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction
      )

    def userThreadsThreadId =
      foreignKey("user_threads_thread_id", userId, ThreadsTable.table)(
        _.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction
      )

    override def * =
      (id, userId, threadId, isClose, createAt, closeAt) <>
        ((UserThreadsRecord.apply _).tupled, UserThreadsRecord.unapply)
  }
}

