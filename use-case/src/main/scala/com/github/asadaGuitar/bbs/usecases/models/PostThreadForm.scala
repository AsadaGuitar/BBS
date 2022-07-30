package com.github.asadaGuitar.bbs.usecases.models

import com.github.asadaGuitar.bbs.domains.models.{ThreadTitle, UserId}

final case class PostThreadForm(userId: UserId,
                                title: ThreadTitle,
                                otherUserIds: List[UserId])
