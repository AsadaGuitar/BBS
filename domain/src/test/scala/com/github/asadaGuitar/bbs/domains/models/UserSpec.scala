package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.{AnyWordSpec, AsyncWordSpec}

import scala.concurrent.Future


final class UserSpec extends AnyWordSpec {}

final class UserIdSpec extends AnyWordSpec {

  "UserId.apply" should {

    "succeeds when the value length is 12." in {
      assert {
        UserId(Utils.generateRandomString(12))
        true
      }
    }

    "fails on the value is shorter required length." in {
      assertThrows[IllegalArgumentException] {
        UserId(Utils.generateRandomString(11))
      }
    }

    "fails on the value is longer required length." in {
      assertThrows[IllegalArgumentException] {
        UserId(Utils.generateRandomString(13))
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        UserId("")
      }
    }
  }
}

final class UserNameSpec extends AnyWordSpec {

  "UserName.apply" should {

    "succeeds when the value length is 2." in {
      assert {
        UserName(Utils.generateRandomString(2))
        true
      }
    }

    "succeeds when the value length is 50." in {
      assert {
        UserName(Utils.generateRandomString(50))
        true
      }
    }

    "succeeds when the value length is 25." in {
      assert {
        UserName(Utils.generateRandomString(25))
        true
      }
    }

    "fails on the value is shorter required length." in {
      assertThrows[IllegalArgumentException] {
        UserName(Utils.generateRandomString(1))
      }
    }

    "fails on the value is longer required length." in {
      assertThrows[IllegalArgumentException] {
        UserName(Utils.generateRandomString(51))
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        UserName("")
      }
    }
  }
}

final class UserPasswordSpec extends AsyncWordSpec {

  "Plain.apply" should {

    "succeeds when the value length is 8." in {
      assert {
        PlainPassword("Passw0rd")
        true
      }
    }

    "succeeds when the value length is 100." in {
      assert {
        val password = "abcdefghijklmnopqistuvws" +
          "yzabcdefghijklmnopqistuvwsyzabcdefg" +
          "hijklmnopqistuvwsyzABCDEFGHIJKL0123456789"
        PlainPassword(password)
        password.length === 100
      }
    }

    "fails on the value is only english university text." in {
      assertThrows[IllegalArgumentException] {
        PlainPassword("TESTTEST")
      }
    }

    "fails on the value is only english small text." in {
      assertThrows[IllegalArgumentException] {
        PlainPassword("testtest")
      }
    }

    "fails on the value is only numbers." in {
      assertThrows[IllegalArgumentException] {
        PlainPassword("12341234")
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        PlainPassword("")
      }
    }
  }

  "Plain.bcryptSafeBounded" should {

    "generate Bcrypted." in {
      val plain = PlainPassword("Passw0rd").bcryptSafeBounded
      assert(plain.isSuccess)
    }
  }

  "Bcrypted.apply" should {

    "succeeds when the value length is any string." in {
      assert {
        BcryptedPassword("hello")
        true
      }
    }
  }

  "UserPassword" should {

    "encoded values can be verified." in {
      val plain = PlainPassword("Passw0rd0")
      val bcryptedTry = plain.bcryptSafeBounded
      for {
        isValid0 <- Future.fromTry{
          bcryptedTry.flatMap{ bcrypted =>
            bcrypted.verify(plain)
          }
        }
        isValid1 <- Future.fromTry{
          bcryptedTry.flatMap{ bcrypted =>
            bcrypted.verify(PlainPassword("Passw0rd1"))
          }
        }
      } yield assert(isValid0 && !isValid1)
    }
  }
}
