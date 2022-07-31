package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{User, UserId}
import com.github.asadaGuitar.bbs.repositories.models.UserForm

import scala.concurrent.Future

trait UsersRepository {

  def save(userForm: UserForm): Future[Int]

  def findById(userId: UserId): Future[Option[User]]

  def existsById(userId: UserId): Future[Boolean]
}
