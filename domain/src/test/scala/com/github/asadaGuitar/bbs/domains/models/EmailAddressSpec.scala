package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class EmailAddressSpec extends AnyWordSpec {

  "EmailAddress.apply" should {

    "succeeds when the value is email address." in {
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
}
