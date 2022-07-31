package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.Functor
import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, ThreadTitle, UserId}
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import com.github.asadaGuitar.bbs.repositories.ThreadsRepository
import com.github.asadaGuitar.bbs.repositories.models.ThreadForm

import java.sql.Timestamp
import java.util.Date
import scala.concurrent.{ExecutionContext, Future}

final class SlickThreadsRepositoryImpl(implicit ec: ExecutionContext) extends ThreadsRepository with SlickDbConfigProvider {

  import dbConfig.profile.api._

  override def save(threadForm: ThreadForm): Future[Int] = threadForm match {
    case ThreadForm(id, userId, title) =>
      dbConfig.db.run {
        Tables.Threads
          .insertOrUpdate {
            Tables.ThreadsRow(
              id = id.value,
              userId = userId.value,
              title = title.value,
              createAt = new Timestamp(new Date().getTime)
            )
          }
      }
  }

  override def findById(threadId: ThreadId): Future[Option[Thread]] =
    convertThreadRecordToThead {
      dbConfig.db.run {
        Tables.Threads.filter(thread => thread.id === threadId.value && !thread.isClose).result.headOption
      }
    }

  override def findAllByUserId(userId: UserId): Future[List[Thread]] =
    convertThreadRecordToThead {
      dbConfig.db.run {
        Tables.Threads.filter(thread => thread.userId === userId.value && !thread.isClose).result
      }
    }.map(_.toList)

  override def existsById(threadId: ThreadId): Future[Boolean] =
    dbConfig.db.run {
      Tables.Threads.filter(thread => thread.id === threadId.value && !thread.isClose).exists.result
    }

  private def convertThreadRecordToThead[F[_]](threadsRow: Future[F[Tables.ThreadsRow]])
                                              (implicit fa: Functor[F]): Future[F[Thread]] =
    for {
      record <- threadsRow
    } yield fa.map(record) {
      case Tables.ThreadsRow(id, userId, title, isClose, createAt, modifyAt, closeAt) =>
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