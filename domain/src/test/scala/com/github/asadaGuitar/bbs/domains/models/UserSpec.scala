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

  "UserId.matches" should {

    "succeeds when the value length is 12." in {
      assert(UserId.matches(Utils.generateRandomString(12)))
    }

    "fails on the value is shorter required length." in {
      assert(!UserId.matches(Utils.generateRandomString(11)))
    }

    "fails on the value is longer required length." in {
      assert(!UserId.matches(Utils.generateRandomString(13)))
    }

    "fails on empty values." in {
      assert(!UserId.matches(""))
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

  "UserName.matches" should {

    "succeeds when the value length is 2." in {
      assert {
        UserName.matches(Utils.generateRandomString(2))
        true
      }
    }

    "succeeds when the value length is 50." in {
      assert {
        UserName.matches(Utils.generateRandomString(50))
        true
      }
    }

    "succeeds when the value length is 25." in {
      assert {
        UserName.matches(Utils.generateRandomString(25))
        true
      }
    }

    "fails on the value is shorter required length." in {
      assert(!UserName.matches(Utils.generateRandomString(1)))
    }

    "fails on the value is longer required length." in {
      assert(!UserName.matches(Utils.generateRandomString(51)))

    }

    "fails on empty values." in {
      assert(!UserName.matches(""))
    }
  }
}

final class UserPasswordSpec extends AsyncWordSpec {

  "UserPassword.apply" should {

    "succeeds when the value length is 8." in {
      assert {
        UserPassword("Passw0rd")
        true
      }
    }

    "succeeds when the value length is 100." in {
      assert {
        val password = "abcdefghijklmnopqistuvws" +
          "yzabcdefghijklmnopqistuvwsyzabcdefg" +
          "hijklmnopqistuvwsyzABCDEFGHIJKL0123456789"
        UserPassword(password)
        password.length == 100
      }
    }

    "fails on the value is only english university text." in {
      assertThrows[IllegalArgumentException] {
        UserPassword("TESTTEST")
      }
    }

    "fails on the value is only english small text." in {
      assertThrows[IllegalArgumentException] {
        UserPassword("testtest")
      }
    }

    "fails on the value is only numbers." in {
      assertThrows[IllegalArgumentException] {
        UserPassword("12341234")
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        UserPassword("")
      }
    }
  }

  "UserPassword.matches" should {

    "succeeds when the value length is 8." in {
      assert {
        UserPassword.matches("Passw0rd")
        true
      }
    }

    "succeeds when the value length is 100." in {
      assert {
        val password = "abcdefghijklmnopqistuvws" +
          "yzabcdefghijklmnopqistuvwsyzabcdefg" +
          "hijklmnopqistuvwsyzABCDEFGHIJKL0123456789"
        UserPassword.matches(password)
        password.length == 100
      }
    }

    "fails on the value is only english university text." in {
      assert(!UserPassword.matches("TESTTEST"))
    }

    "fails on the value is only english small text." in {
      assert(!UserPassword.matches("testtest"))
    }

    "fails on the value is only numbers." in {
      assertThrows[IllegalArgumentException] {
        assert(!UserPassword.matches("12341234"))
      }
    }

    "fails on empty values." in {
      assert(!UserPassword.matches(""))
    }
  }

  "UserPassword.crypted" should {

    "succeeds on generate bcrypted value." in {
      val userPassword = UserPassword("Passw0rd")
      Future.fromTry {
        userPassword.crypted.map { crypted =>
          assert(userPassword.plain !== crypted)
        }
      }
    }
  }

  "UserPassword.verify" should {

    "the values to be encoded in bcrypt will be the same" in {
      val userPassword = UserPassword("Passw0rd")
      Future.fromTry(userPassword.verify(userPassword.plain))
        .map(assert(_))
    }

    "fails on verified different password as UserPassword" in {
      val userPassword = UserPassword("Passw0rd")
      val userInvalidPassword = UserPassword("Passw1rd")
      Future.fromTry(userPassword.verify(userInvalidPassword))
        .map(result => assert(!result))
    }

    "fails on verified different password as String" in {
      val userPassword = UserPassword("Passw0rd")
      val userInvalidPassword = "Passw1rd"
      Future.fromTry(userPassword.verify(userInvalidPassword))
        .map(result => assert(!result))
    }
  }
}
