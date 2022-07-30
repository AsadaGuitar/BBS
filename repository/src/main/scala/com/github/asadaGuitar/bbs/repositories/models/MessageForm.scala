package com.github.asadaGuitar.bbs.repositories.models

import com.github.asadaGuitar.bbs.domains.models.{MessageId, MessageText, ThreadId, UserId}

final case class MessageForm(id: MessageId,
                             threadId: ThreadId,
                             userId: UserId,
                             text: MessageText)
