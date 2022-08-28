package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class JwtSpec extends AnyWordSpec {

  "Jwt.apply" should {

    "succeeds when the value is jwt." in {
      assert {
        Jwt(
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9." +
            "TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        )
        true
      }
    }

    "fails on the value is not jwt." in {
      assertThrows[IllegalArgumentException] {
        Jwt("test")
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        Jwt("")
      }
    }
  }
}
