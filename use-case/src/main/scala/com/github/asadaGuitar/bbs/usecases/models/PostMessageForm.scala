package com.github.asadaGuitar.bbs.usecases.models

import com.github.asadaGuitar.bbs.domains.models.{MessageText, ThreadId, UserId}

final case class PostMessageForm(threadId: ThreadId,
                                 userId: UserId,
                                 text: MessageText)
