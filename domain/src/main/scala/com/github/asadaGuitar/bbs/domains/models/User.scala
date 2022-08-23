package com.github.asadaGuitar.bbs.domains.models

import cats.implicits.catsSyntaxEq

import java.time.Instant
import scala.concurrent.ExecutionContext
import scala.util.Try

final case class User(id: UserId,
                      firstName: UserName,
                      lastName: UserName,
                      emailAddress: EmailAddress,
                      password: UserPassword,
                      isClose: Boolean = false,
                      createAt: Instant = Instant.now(),
                      modifyAt: Option[Instant] = None,
                      closeAt: Option[Instant] = None)

object UserId {

  def matches(value: String): Boolean = 12 === value.length
}

final case class UserId(value: String) {

  import UserId._

  require(matches(value))
}

object UserName {

  def matches(value: String): Boolean = 2 <= value.length && value.length <= 50
}

final case class UserName(value: String) {

  import UserName._

  require(matches(value))
}

object UserPassword {

  def matches(value: String): Boolean = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)[a-zA-Z\\d]{8,100}$".r.matches(value)
}

final case class UserPassword(plain: String) {

  import UserPassword._
  import com.github.t3hnar.bcrypt._

  require(matches(plain))

  val crypted: Try[String] = plain.bcryptSafeBounded

  def verify(other: UserPassword)(implicit ec: ExecutionContext): Try[Boolean] =
    this.crypted.flatMap {
      other.plain.isBcryptedSafeBounded
    }

  def verify(plain: String)(implicit ec: ExecutionContext): Try[Boolean] =
    this.crypted.flatMap {
      plain.isBcryptedSafeBounded
    }
}
