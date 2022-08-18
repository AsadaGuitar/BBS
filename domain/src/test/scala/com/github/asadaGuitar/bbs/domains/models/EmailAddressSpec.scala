package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.flatspec.AnyFlatSpec

final class EmailAddressSpec extends AnyFlatSpec {

  "EmailAddress" should "succeeds when the value is correct." in {
    assert {
      try { EmailAddress("info@sample.com"); true }
      catch {
        case _: IllegalArgumentException => false
      }
    }
  }

  "EmailAddress" should "fails on invalid values." in {
    assert {
      try { EmailAddress("test"); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }

  "EmailAddress" should "fails on empty values." in {
    assert {
      try { EmailAddress(""); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }
}
