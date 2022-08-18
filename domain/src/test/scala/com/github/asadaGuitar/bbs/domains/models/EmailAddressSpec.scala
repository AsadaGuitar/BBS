package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class EmailAddressSpec extends AnyWordSpec {

  "EmailAddress" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          EmailAddress("info@sample.com")
          true
        }
        catch {
          case _: IllegalArgumentException => false
        }
      }
    }

    "fails on invalid values." in {
      assert {
        try {
          EmailAddress("test")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }

    "fails on empty values." in {
      assert {
        try {
          EmailAddress("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}
