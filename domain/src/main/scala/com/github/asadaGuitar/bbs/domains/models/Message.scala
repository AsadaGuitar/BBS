package com.github.asadaGuitar.bbs.domains.models

import cats.implicits.catsSyntaxEq

import java.time.Instant

final case class Message(id: MessageId,
                         threadId: ThreadId,
                         userId: UserId,
                         text: MessageText,
                         isClose: Boolean = false,
                         createAt: Instant = Instant.now(),
                         modifyAt: Option[Instant] = None,
                         closeAt: Option[Instant] = None)

object MessageId {

  def matches(value: String): Boolean = 12 === value.length
}

final case class MessageId(value: String) {

  import MessageId._

  require(matches(value))
}

object MessageText {

  def matches(value: String): Boolean = 1 <= value.length && value.length <= 1024
}

final case class MessageText(value: String) {

  import MessageText._

  require(matches(value))
}
