package com.github.asadaGuitar.bbs.domains.models

import scala.util.matching.Regex


object EmailAddress {
  val emailPattern: Regex = "".r
}

final case class EmailAddress(value: String) {
  import EmailAddress._
  require(emailPattern.matches(value))
}
