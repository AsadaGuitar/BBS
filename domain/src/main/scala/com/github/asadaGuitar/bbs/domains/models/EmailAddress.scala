package com.github.asadaGuitar.bbs.domains.models

import scala.util.matching.Regex


object EmailAddress {
  val emailPattern: Regex = "^[a-zA-Z0-9_+-]+(.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$".r
}

final case class EmailAddress(value: String) {
  import EmailAddress._
  require(emailPattern.matches(value))
}
