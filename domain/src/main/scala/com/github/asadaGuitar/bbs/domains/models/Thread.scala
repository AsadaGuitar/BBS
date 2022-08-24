package com.github.asadaGuitar.bbs.domains.models

import cats.implicits.catsSyntaxEq

import java.time.Instant

final case class Thread(
    id: ThreadId,
    userId: UserId,
    title: ThreadTitle,
    isClose: Boolean = false,
    createAt: Instant = Instant.now(),
    modifyAt: Option[Instant] = None,
    closeAt: Option[Instant] = None
)

object ThreadId {

  def matches(value: String): Boolean = 12 === value.length
}

final case class ThreadId(value: String) {

  import ThreadId._

  require(matches(value))
}

object ThreadTitle {

  def matches(value: String): Boolean =
    8 <= value.length && value.length <= 255
}

final case class ThreadTitle(value: String) {

  import ThreadTitle._

  require(matches(value))
}
