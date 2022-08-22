package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class EmailAddressSpec extends AnyWordSpec {

  "EmailAddress.apply" should {

    "succeeds when the value is correct." in {
      assert {
        EmailAddress("info@sample.com")
        true
      }
    }

    "fails on invalid values." in {
      assertThrows[IllegalArgumentException] {
        EmailAddress("test")
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        EmailAddress("")
      }
    }
  }

  "EmailAddress.matches" should {

    "returns true as `Boolean` when a value is email address." in {
      assert(EmailAddress.matches("info@sample.com"))
    }

    "returns false as `Boolean` when a value is invalided." in {
      assert(!EmailAddress.matches("test"))
    }

    "returns false as `Boolean` when a value is empty." in {
      assert(!EmailAddress.matches(""))
    }

  }
}
