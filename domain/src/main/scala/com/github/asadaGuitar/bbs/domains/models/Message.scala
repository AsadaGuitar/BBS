package com.github.asadaGuitar.bbs.domains.models

import cats.implicits.catsSyntaxEq
import com.typesafe.config.ConfigFactory

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

  private val lengthRequired =
    ConfigFactory.load().getInt("application.domain.message.id.length")

  def matches(value: String): Boolean =
    lengthRequired === value.length
}

final case class MessageId(value: String) {

  import MessageId._

  require(matches(value))
}

object MessageText {

  private val (minLengthRequired, maxLengthRequired) = {
    val config = ConfigFactory.load()
    (config.getInt("application.domain.message.text.length.min"),
      config.getInt("application.domain.message.text.length.max"))
  }

  def matches(value: String): Boolean =
    minLengthRequired <= value.length && value.length <= maxLengthRequired
}

final case class MessageText(value: String) {

  import MessageText._

  require(matches(value))
}
