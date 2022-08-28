package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.Functor
import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, ThreadTitle, UserId}
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import cats.implicits._
import com.github.asadaGuitar.bbs.domains.repositories.ThreadsRepository

import scala.concurrent.{ExecutionContext, Future}

final class SlickThreadsRepositoryImpl(implicit ec: ExecutionContext)
    extends ThreadsRepository
    with SlickDbConfigProvider
    with DbColumnsConvertor {

  import dbConfig.profile.api._

  override def save(thread: Thread): Future[Int] = {
    val Thread(id, userId, title, isClose, createAt, modifyAt, closeAt) = thread
    dbConfig.db.run {
      Tables.Threads
        .insertOrUpdate {
          Tables.ThreadsRow(
            id = id.value,
            userId = userId.value,
            title = title.value,
            isClose = isClose,
            createAt = createAt,
            modifyAt = modifyAt,
            closeAt = closeAt
          )
        }
    }
  }

  override def findById(threadId: ThreadId): Future[Option[Thread]] =
    convertThreadsRowsToThead {
      dbConfig.db.run {
        Tables.Threads
          .filter(thread => thread.id === threadId.value && !thread.isClose)
          .result
          .headOption
      }
    }

  override def findAllByUserId(userId: UserId): Future[Vector[Thread]] =
    convertThreadsRowsToThead {
      dbConfig.db.run {
        Tables.Threads
          .filter(thread => thread.userId === userId.value && !thread.isClose)
          .result
      }
    }.map(_.toVector)

  override def existsById(threadId: ThreadId): Future[Boolean] =
    dbConfig.db.run {
      Tables.Threads
        .filter(thread => thread.id === threadId.value && !thread.isClose)
        .exists
        .result
    }

  private def convertThreadsRowsToThead[F[_]](
      threadsRows: Future[F[Tables.ThreadsRow]]
  )(implicit ff: Functor[F]): Future[F[Thread]] =
    for {
      rows <- threadsRows
    } yield ff.map(rows) { row =>
      val Tables.ThreadsRow(id, userId, title, isClose, createAt, modifyAt, closeAt) = row
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
