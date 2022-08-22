package com.github.asadaGuitar.bbs.domains.models

import java.time.Instant
import scala.concurrent.ExecutionContext
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

final case class UserPassword(plain: String) {
  import com.github.t3hnar.bcrypt._

  require(plain.nonEmpty)

  val crypted: Try[String] = plain.bcryptSafeBounded

  def ?=(other: UserPassword)(implicit ec: ExecutionContext): Try[Boolean] =
    this.crypted.flatMap {
      other.plain.isBcryptedSafeBounded
    }

  def ?=(plain: String)(implicit ec: ExecutionContext): Try[Boolean] =
    this.crypted.flatMap {
      plain.isBcryptedSafeBounded
    }
}
