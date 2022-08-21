package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.models.{ThreadId, UserId, UserThreads, UserThreadsId}
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import com.github.asadaGuitar.bbs.repositories.UserThreadsRepository

import scala.concurrent.{ExecutionContext, Future}

final class SlickUserThreadsRepositoryImpl(implicit ec: ExecutionContext)
    extends UserThreadsRepository
    with SlickDbConfigProvider {

  import dbConfig.profile.api._

  private val convertUserThreadsRowToUserThreads: Tables.UserThreads#TableElementType => UserThreads =
    (userThreadsRow: Tables.UserThreads#TableElementType) => {
      val Tables.UserThreadsRow(id, userId, threadsId, isClose, createAt, closeAt) = userThreadsRow
      UserThreads(id = UserThreadsId(id), userId = UserId(userId), threadId = ThreadId(threadsId), isClose = isClose, createAt = createAt, closeAt = closeAt)
    }

  override def save(userThreads: UserThreads): Future[Int] = {
    val UserThreads(id, userId, threadId, isClose, createAt, closeAt) = userThreads
    dbConfig.db.run {
      Tables.UserThreads
        .insertOrUpdate {
          Tables.UserThreadsRow(
            id = id match {
              case UserThreadsId.Just(value) => value
              case UserThreadsId.Nothing     => 0
            },
            userId = userId.value,
            threadId = threadId.value,
            isClose = isClose,
            createAt = createAt,
            closeAt = closeAt
          )
        }
    }
  }

  override def findAllByUserId(userId: UserId): Future[Vector[UserThreads]] =
    dbConfig.db
      .run {
        Tables.UserThreads
          .filter(_.userId === userId.value)
          .result
      }.map(_.toVector.map(convertUserThreadsRowToUserThreads))
}
