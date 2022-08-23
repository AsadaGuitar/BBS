package com.github.asadaGuitar.bbs.domains.models

object Jwt {

  def matches(value: String): Boolean =
    "^[a-zA-Z\\d].*\\.[a-zA-Z\\d].*\\.[a-zA-Z\\d].*$".r.matches(value)
}

final case class Jwt(value: String) {

  import Jwt._

  require(matches(value))
}
