package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.flatspec.AnyFlatSpec

final class JwtTokenSpec extends AnyFlatSpec {

  "JwtToken" should "succeeds when the value is correct." in {
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

  "JwtToken" should "fails on invalid values." in {
    assert {
      try { JwtToken("test"); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }

  "JwtToken" should "fails on empty values." in {
    assert {
      try { JwtToken(""); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }
}
