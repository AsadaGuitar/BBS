package com.github.asadaGuitar.bbs.domains.models

import java.util.Date

final case class Message(id: MessageId,
                         threadId: ThreadId,
                         userId: UserId,
                         text: MessageText,
                         isClose: Boolean,
                         createAt: Date,
                         modifyAt: Option[Date],
                         closeAt: Option[Date])

final case class MessageId(value: String) {
  require(value.nonEmpty)
}

final case class MessageText(value: String) {
  require(value.nonEmpty)
}