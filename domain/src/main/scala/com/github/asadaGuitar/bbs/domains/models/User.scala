package com.github.asadaGuitar.bbs.domains.models

import java.time.Instant
import scala.concurrent.Future
import scala.util.Try

final case class User(
    id: UserId,
    firstName: UserName,
    lastName: UserName,
    emailAddress: EmailAddress,
    password: UserPassword,
    isClose: Boolean = false,
    createAt: Instant = Instant.now(),
    modifyAt: Option[Instant] = None,
    closeAt: Option[Instant] = None
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
