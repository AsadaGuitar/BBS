package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.models.ThreadForm

import scala.concurrent.Future


object ThreadsRepository {
}

trait ThreadsRepository {
  import ThreadsRepository._

  // 要件番号【4】：サインイン済みのユーザーアカウントを用いて、新規にスレッドを作成できる
  def save(threadForm: ThreadForm): Future[Int]

  // 要件番号【7】：サインイン済みのユーザーアカウントを用いて、参加しているスレッドを 1 つ選び、メッセージを一覧で取得できる
  def findById(threadId: ThreadId): Future[Option[Thread]]

  // 要件番号【5】：サインイン済みのユーザーアカウントを用いて、参加しているスレッドを一覧で取得できる
  def findAllByUserId(userId: UserId): Future[List[Thread]]

  def existsById(threadId: ThreadId): Future[Boolean]
}
