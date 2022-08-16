package com.github.asadaGuitar.bbs.domains.models

import java.time.Instant

final case class UserThreads(
    id: UserThreadsId = UserThreadsId.Nothing,
    userId: UserId,
    threadId: ThreadId,
    isClose: Boolean = false,
    createAt: Instant = Instant.now(),
    closeAt: Option[Instant] = None
)

sealed trait UserThreadsId

object UserThreadsId {
  final case class Just(value: Int) extends UserThreadsId {
    require(!value.isNaN && (value != 0))
  }
  case object Nothing extends UserThreadsId
}
