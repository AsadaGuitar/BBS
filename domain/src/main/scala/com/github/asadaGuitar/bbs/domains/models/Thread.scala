package com.github.asadaGuitar.bbs.domains.models

import java.util.Date

final case class Thread(
    id: ThreadId,
    userId: UserId,
    title: ThreadTitle,
    isClose: Boolean,
    createAt: Date,
    modifyAt: Option[Date],
    closeAt: Option[Date]
)

final case class ThreadId(value: String) {
  require(value.nonEmpty)
}

final case class ThreadTitle(value: String) {
  require(value.nonEmpty)
}
