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

  require(12 == value.length)
}

final case class ThreadTitle(value: String) {

  require(8 <= value.length && value.length <= 255)
}
