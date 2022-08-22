package com.github.asadaGuitar.bbs.domains.models

import cats.implicits.catsSyntaxEq
import com.typesafe.config.ConfigFactory

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

  private val lengthRequired =
    ConfigFactory.load().getInt("application.domain.user.id.length")

  def matches(value: String): Boolean =
    lengthRequired === value.length
}

final case class UserId(value: String) {

  import UserId._

  require(matches(value))
}

object UserName {

  private val (minLengthRequired, maxLengthRequired) ={
    val config = ConfigFactory.load()
    (config.getInt("application.domain.user.name.length.min"),
      config.getInt("application.domain.user.name.length.max"))
  }

  def matches(value: String): Boolean =
    minLengthRequired <= value.length && value.length <= maxLengthRequired
}

final case class UserName(value: String) {

  import UserName._

  require(matches(value))
}

object UserPassword {

  private val patternRequired =
    ConfigFactory.load().getString("application.domain.user.password.pattern").r

  def matches(value: String): Boolean =
    patternRequired.matches(value)

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
