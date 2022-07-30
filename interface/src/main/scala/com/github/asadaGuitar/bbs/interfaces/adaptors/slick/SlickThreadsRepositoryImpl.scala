package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.Functor
import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, ThreadTitle, UserId}
import com.github.asadaGuitar.bbs.repositories.ThreadsRepository
import com.github.asadaGuitar.bbs.repositories.models.ThreadForm

import java.sql.Date
import java.util.{Date => UtilDate}
import scala.concurrent.{ExecutionContext, Future}

final class SlickThreadsRepositoryImpl(implicit ec: ExecutionContext) extends ThreadsRepository with SlickDbConfigProvider {

  import dbConfig.profile.api._

  override def save(threadForm: ThreadForm): Future[Int] = threadForm match {
    case ThreadForm(id, userId, title) =>
      dbConfig.db.run {
        ThreadsTable.table
          .insertOrUpdate {
            ThreadsRecord(id = id.value, userId = userId.value, title = title.value)
          }
      }
  }

  override def findById(threadId: ThreadId): Future[Option[Thread]] =
    convertThreadRecordToThead {
      dbConfig.db.run {
        ThreadsTable.table.filter(thread => thread.id === threadId.value && !thread.isClose).result.headOption
      }
    }

  override def findAllByUserId(userId: UserId): Future[List[Thread]] =
    convertThreadRecordToThead {
      dbConfig.db.run {
        ThreadsTable.table.filter(thread => thread.userId === userId.value && !thread.isClose).result
      }
    }.map(_.toList)

  override def existsById(threadId: ThreadId): Future[Boolean] =
    dbConfig.db.run {
      ThreadsTable.table.filter(thread => thread.id === threadId.value && !thread.isClose).exists.result
    }

  private def convertThreadRecordToThead[F[_]](threadsRecord: Future[F[ThreadsRecord]])
                                              (implicit fa: Functor[F]): Future[F[Thread]] =
    for {
      record <- threadsRecord
    } yield fa.map(record) {
      case ThreadsRecord(id, userId, title, isClose, createAt, modifyAt, closeAt) =>
        Thread(
          id = ThreadId(id),
          userId = UserId(userId),
          title = ThreadTitle(title),
          isClose = isClose,
          createAt = createAt,
          modifyAt = modifyAt,
          closeAt = closeAt
        )
    }
}

private[adaptors] case class ThreadsRecord(id: String,
                                           userId: String,
                                           title: String,
                                           isClose: Boolean = false,
                                           createAt: Date = {
                                             val now = new UtilDate()
                                             new Date(now.getTime)
                                           },
                                           modifyAt: Option[Date] = None,
                                           closeAt: Option[Date] = None)

private[adaptors] object ThreadsTable extends SlickDbConfigProvider {
  import dbConfig.profile.api._

  val table = TableQuery[ThreadsTable]

  private[adaptors] class ThreadsTable(tag: Tag) extends Table[ThreadsRecord](tag, "threads") {
    def id = column[String]("id")

    def userId = column[String]("user_id")

    def title = column[String]("title")

    def isClose = column[Boolean]("is_close")

    def createAt = column[java.sql.Date]("create_at")

    def modifyAt = column[Option[java.sql.Date]]("modify_at")

    def closeAt = column[Option[java.sql.Date]]("close_at")

    def supplier =
      foreignKey("user_threads_pk", userId, ThreadsTable.table)(
        _.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction
      )

    override def * =
      (id, userId, title, isClose, createAt, modifyAt, closeAt) <>
        ((ThreadsRecord.apply _).tupled, ThreadsRecord.unapply)
  }
}


