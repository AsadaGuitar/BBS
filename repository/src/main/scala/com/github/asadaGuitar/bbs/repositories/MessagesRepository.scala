package com.github.asadaGuitar.bbs.repositories

import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, ThreadId}
import com.github.asadaGuitar.bbs.repositories.models.MessageForm

import scala.concurrent.Future


object MessagesRepository {
}

trait MessagesRepository {
  import MessagesRepository._

  // 要件番号【6】：サインイン済みのユーザーアカウントを用いて、参加しているスレッドを 1 つ選び、メッセージを投稿できる
  def save(messageForm: MessageForm): Future[Int]

  // 要件番号【7】：サインイン済みのユーザーアカウントを用いて、参加しているスレッドを 1 つ選び、メッセージを一覧で取得できる
  def findAllByThreadId(threadId: ThreadId): Future[List[Message]]

  def existsById(messageId: MessageId): Future[Boolean]
}
