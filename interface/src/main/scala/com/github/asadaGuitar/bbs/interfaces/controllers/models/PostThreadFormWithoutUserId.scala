package com.github.asadaGuitar.bbs.interfaces.controllers.models

import com.github.asadaGuitar.bbs.domains.models.{ ThreadTitle, UserId }

final case class PostThreadFormWithoutUserId(title: ThreadTitle, otherUserIds: List[UserId])
