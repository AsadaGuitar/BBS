package com.github.asadaGuitar.bbs.domains.models

import java.util.Date
import scala.concurrent.Future
import scala.util.Try

final case class User(
    id: UserId,
    firstName: UserName,
    lastName: UserName,
    emailAddress: EmailAddress,
    password: UserPassword,
    isClose: Boolean = true,
    createAt: Date,
    modifyAt: Option[Date],
    closeAt: Option[Date]
)

final case class UserId(value: String) {
  require(value.nonEmpty)
}

final case class UserName(value: String) {
  require(value.nonEmpty)
}

final case class UserPassword(value: String) {
  import com.github.t3hnar.bcrypt._
  require(value.nonEmpty)

  def bcryptBounded: Try[UserPassword] = value.bcryptSafeBounded.map(UserPassword)

  def bcryptBoundedFuture: Future[UserPassword] = Future.fromTry(bcryptBounded)
}
