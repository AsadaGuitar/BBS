package com.github.asadaGuitar.bbs.domains.models

import java.time.Instant

final case class UserThreads(
    id: UserThreadsId = UserThreadsId(0),
    userId: UserId,
    threadId: ThreadId,
    isClose: Boolean = false,
    createAt: Instant = Instant.now(),
    closeAt: Option[Instant] = None
)

final case class UserThreadsId(value: Long)
