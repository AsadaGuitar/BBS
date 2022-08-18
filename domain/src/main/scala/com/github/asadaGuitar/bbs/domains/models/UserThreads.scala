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

  def apply(value: Int): UserThreadsId = value match {
    case n if n.isNaN || n == 0 => Nothing
    case otherwise              => Just(otherwise)
  }

  final case class Just(value: Int) extends UserThreadsId {
    require(!value.isNaN && (value != 0))
  }

  case object Nothing extends UserThreadsId
}
