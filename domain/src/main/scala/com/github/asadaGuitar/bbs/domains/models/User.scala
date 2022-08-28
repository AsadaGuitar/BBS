package com.github.asadaGuitar.bbs.domains.models

import java.time.Instant
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

final case class UserId(value: String) {

  require(12 == value.length)
}

final case class UserName(value: String) {

  require(2 <= value.length && value.length <= 50)
}

sealed trait UserPassword {
  def value: String
}

final case class BcryptedPassword(value: String) extends UserPassword {

  import com.github.t3hnar.bcrypt._

  def verify(plain: PlainPassword): Try[Boolean] =
    plain.value.isBcryptedSafeBounded(value)
}

object PlainPassword {
  private val pattern = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)[a-zA-Z\\d]{8,100}$".r
}

final case class PlainPassword(value: String) extends UserPassword {

  import PlainPassword._
  import com.github.t3hnar.bcrypt._

  require(pattern.matches(value))

  def bcryptSafeBounded: Try[BcryptedPassword] = value.bcryptSafeBounded.map(BcryptedPassword)
}