package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.models.{ ThreadId, UserId }
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import com.github.asadaGuitar.bbs.repositories.UserThreadsRepository
import com.github.asadaGuitar.bbs.repositories.models.UserThreadsForm

import java.util.Date
import java.sql.Timestamp
import scala.concurrent.{ ExecutionContext, Future }

final class SlickUserThreadsRepositoryImpl(implicit ec: ExecutionContext)
    extends UserThreadsRepository
    with SlickDbConfigProvider {

  import dbConfig.profile.api._

  override def save(userThreadsForm: UserThreadsForm): Future[Int] =
    userThreadsForm match {
      case UserThreadsForm(userId, threadId) =>
        dbConfig.db.run {
          Tables.UserThreads.insertOrUpdate {
            Tables.UserThreadsRow(
              id = 0,
              userId = userId.value,
              threadId = threadId.value,
              createAt = new Timestamp(new Date().getTime)
            )
          }
        }
    }

  override def findAllByUserId(userId: UserId): Future[List[ThreadId]] =
    dbConfig.db
      .run {
        Tables.UserThreads
          .filter(_.userId === userId.value)
          .map(_.threadId)
          .result
      }.map(_.toList.map(ThreadId))
}
