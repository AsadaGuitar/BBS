package com.github.asadaGuitar.bbs.domains.models


import java.time.Instant

final case class Message(id: MessageId,
                         threadId: ThreadId,
                         userId: UserId,
                         text: MessageText,
                         isClose: Boolean = false,
                         createAt: Instant = Instant.now(),
                         modifyAt: Option[Instant] = None,
                         closeAt: Option[Instant] = None)

final case class MessageId(value: String) {

  require(12 == value.length)
}

final case class MessageText(value: String) {

  require(1 <= value.length && value.length <= 1024)
}
