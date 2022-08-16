package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, User, UserId }

import scala.concurrent.Future

trait UsersRepository {

  def save(user: User): Future[Int]

  def findById(userId: UserId): Future[Option[User]]

  def existsById(userId: UserId): Future[Boolean]

  def existsByEmailAddress(mailAddress: EmailAddress): Future[Boolean]
}
