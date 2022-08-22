package com.github.asadaGuitar.bbs.domains.models

import org.apache.commons.validator.routines.EmailValidator

object EmailAddress {

  private val validator: EmailValidator = EmailValidator.getInstance()

  def matches(value: String): Boolean = validator.isValid(value)
}

final case class EmailAddress(value: String) {

  import EmailAddress._

  require(matches(value))
}
