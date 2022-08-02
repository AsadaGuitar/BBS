package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{ Thread, ThreadId, UserId }
import com.github.asadaGuitar.bbs.repositories.models.ThreadForm

import scala.concurrent.Future

trait ThreadsRepository {

  def save(threadForm: ThreadForm): Future[Int]

  def findById(threadId: ThreadId): Future[Option[Thread]]

  def findAllByUserId(userId: UserId): Future[List[Thread]]

  def existsById(threadId: ThreadId): Future[Boolean]
}
