package com.github.asadaGuitar.bbs.domains.models

import java.util.Date

final case class User(id: UserId,
                      firstName: UserName,
                      lastName: UserName,
                      emailAddress: EmailAddress,
                      password: UserPassword,
                      isClose: Boolean = true,
                      createAt: Date,
                      modifyAt: Option[Date],
                      closeAt: Option[Date])

final case class UserId(value: String) {
  require(value.nonEmpty)
}

final case class UserName(value: String) {
  require(value.nonEmpty)
}

final case class UserPassword(value: String) {
  require(value.nonEmpty)
}

