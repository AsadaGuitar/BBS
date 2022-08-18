package com.github.asadaGuitar.bbs.domains.models

import org.apache.commons.validator.routines.EmailValidator

object EmailAddress {

  private val validator: EmailValidator = EmailValidator.getInstance()

  def validateEmailAddress(value: String): Boolean = validator.isValid(value)
}

final case class EmailAddress(value: String) {
  import EmailAddress._
  require(validateEmailAddress(value))
}
