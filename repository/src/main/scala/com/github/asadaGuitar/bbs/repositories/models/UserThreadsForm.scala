package com.github.asadaGuitar.bbs.repositories.models

import com.github.asadaGuitar.bbs.domains.models.{ThreadId, UserId}

final case class UserThreadsForm(userId: UserId, threadId: ThreadId)
