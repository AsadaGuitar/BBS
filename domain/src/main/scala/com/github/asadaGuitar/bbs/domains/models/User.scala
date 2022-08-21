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

object UserPassword {
  def apply(value: String): UserPassword = new UserPassword(value)
}

final class UserPassword(_value: String) {
  import com.github.t3hnar.bcrypt._

  require(_value.nonEmpty)

  val crypted: Try[String] = _value.bcryptSafeBounded

  val plain: String = _value

  def ?=(other: UserPassword)(implicit ec: ExecutionContext): Try[Boolean] =
    this.crypted.flatMap{
      other.plain.isBcryptedSafeBounded
    }

  def ?=(plain: String)(implicit ec: ExecutionContext): Try[Boolean] =
    this.crypted.flatMap{
      plain.isBcryptedSafeBounded
    }
}
