package com.github.asadaGuitar.bbs.repositories.models

import com.github.asadaGuitar.bbs.domains.models.{ThreadId, ThreadTitle, UserId}

final case class ThreadForm(id: ThreadId,
                            userId: UserId,
                            title: ThreadTitle)
