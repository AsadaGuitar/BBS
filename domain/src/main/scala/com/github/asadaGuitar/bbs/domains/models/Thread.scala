package com.github.asadaGuitar.bbs.domains.models

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

final case class ThreadId(value: String) {
  require(value.nonEmpty)
}

final case class ThreadTitle(value: String) {
  require(value.nonEmpty)
}
