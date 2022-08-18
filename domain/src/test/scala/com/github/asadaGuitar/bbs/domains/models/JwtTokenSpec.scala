package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class JwtTokenSpec extends AnyWordSpec {

  "JwtToken" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          JwtToken(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
              "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9." +
              "TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
          )
          true
        } catch {
          case _: IllegalArgumentException => false
        }
      }
    }

    "fails on invalid values." in {
      assert {
        try {
          JwtToken("test")
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
          JwtToken("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}
