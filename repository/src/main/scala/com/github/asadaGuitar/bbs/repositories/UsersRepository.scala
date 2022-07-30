package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{User, UserId}
import com.github.asadaGuitar.bbs.repositories.models.UserForm

import scala.concurrent.Future

trait UsersRepository {

  // 要件番号【1】：サインアップすることで、新規にユーザーアカウントを作成できる
  def save(userForm: UserForm): Future[Int]

  // 要件番号【3】：サインイン済みのユーザーアカウントを用いて、他のユーザーアカウントを一覧で取得できる
  def findById(userId: UserId): Future[Option[User]]

  def existsById(userId: UserId): Future[Boolean]

}
