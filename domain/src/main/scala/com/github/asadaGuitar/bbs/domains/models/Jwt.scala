package com.github.asadaGuitar.bbs.domains.models

object Jwt {

  private val pattern = "^[a-zA-Z\\d].*\\.[a-zA-Z\\d].*\\.[a-zA-Z\\d].*$".r
}

final case class Jwt(value: String) {

  import Jwt._

  require(pattern.matches(value))
}
