package com.github.asadaGuitar.bbs.interfaces.controllers.models

final case class PostThreadRequest(title: String, others: List[UserIdRequest])
