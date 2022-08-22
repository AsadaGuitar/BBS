package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.{AnyWordSpec, AsyncWordSpec}

import scala.concurrent.Future


final class UserSpec extends AnyWordSpec {}

final class UserIdSpec extends AnyWordSpec {

  "UserId.apply" should {

    "succeeds when the value is correct." in {
      assert {
        UserId("6EDI98FE098A")
        true
      }
    }

    "fails on non 12-character string" in {
      assertThrows[IllegalArgumentException] {
        UserId("TEST")
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

    "succeeds when the value is correct." in {
      assert {
        UserName.apply("Passw0rd")
        true
      }
    }

    "fails on value less than 2 characters" in {
      assertThrows[IllegalArgumentException] {
        UserName("a")
      }
    }

    "fails on more than 50 characters" in {
      assertThrows[IllegalArgumentException] {
        UserName("a")
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

  "UserPassword.apply" should {

    "succeeds when the value is correct." in {
      assert {
        UserPassword("Passw0rd")
        true
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        UserPassword("")
      }
    }

    "encoding is possible." in {
      val userPassword = UserPassword("Passw0rd")
      Future.fromTry {
        userPassword.crypted.map { crypted =>
          assert(userPassword.plain !== crypted)
        }
      }
    }

    "the values to be encoded in bcrypt will be the same" in {
      val userPassword = UserPassword("Passw0rd")
      Future.fromTry(userPassword.verify(userPassword.plain))
        .map(assert(_))
    }

    "verification failure possible as UserPassword" in {
      val userPassword = UserPassword("Passw0rd")
      val userInvalidPassword = UserPassword("Passw1rd")
      Future.fromTry(userPassword.verify(userInvalidPassword))
        .map(result => assert(!result))
    }

    "verification failure possible as String" in {
      val userPassword = UserPassword("Passw0rd")
      val userInvalidPassword = "Passw1rd"
      Future.fromTry(userPassword.verify(userInvalidPassword))
        .map(result => assert(!result))
    }
  }
}
