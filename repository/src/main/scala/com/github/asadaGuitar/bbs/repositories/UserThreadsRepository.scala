package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.models.UserThreadsForm

import scala.concurrent.Future

trait UserThreadsRepository {

  // 要件番号【4】：スレッドに参加する他のユーザーアカウントを指定できる
  def save(userThreadsForm: UserThreadsForm): Future[Int]

  def findAllByUserId(userId: UserId): Future[List[ThreadId]]
}
