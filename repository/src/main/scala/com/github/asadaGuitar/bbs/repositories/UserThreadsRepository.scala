package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{ ThreadId, UserId, UserThreads }

import scala.concurrent.Future

trait UserThreadsRepository {

  def save(userThreads: UserThreads): Future[Int]

  def findAllByUserId(userId: UserId): Future[Vector[UserThreads]]
}
