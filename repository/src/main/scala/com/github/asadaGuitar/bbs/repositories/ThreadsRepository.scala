package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{ Thread, ThreadId, UserId }

import scala.concurrent.Future

trait ThreadsRepository {

  def save(thread: Thread): Future[Int]

  def findById(threadId: ThreadId): Future[Option[Thread]]

  def findAllByUserId(userId: UserId): Future[Vector[Thread]]

  def existsById(threadId: ThreadId): Future[Boolean]
}
