package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.{AnyWordSpec, AsyncWordSpec}

import scala.concurrent.Future


final class UserSpec extends AnyWordSpec {}

final class UserIdSpec extends AnyWordSpec {

  "UserId" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          UserId("6EDI98FE098AP")
          true
        }
        catch {
          case _: IllegalArgumentException => false
        }
      }
    }

    "fails on empty values." in {
      assert {
        try {
          UserId("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}

final class UserNameSpec extends AnyWordSpec {

  "UserName" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          UserName("6EDI98FE098AP")
          true
        }
        catch {
          case _: IllegalArgumentException => false
        }
      }
    }

    "fails on empty values." in {
      assert {
        try {
          UserName("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}

final class UserPasswordSpec extends AsyncWordSpec {

  "UserPassword" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          UserPassword("6EDI98FE098AP")
          true
        }
        catch {
          case _: IllegalArgumentException => false
        }
      }
    }

    "fails on empty values." in {
      assert {
        try {
          UserPassword("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }

    "encoding is possible." in {
      val userPassword = UserPassword("6EDI98FE098AP")
      Future.fromTry {
        userPassword.crypted.map { crypted =>
          assert(userPassword.plain !== crypted)
        }
      }
    }

    "the values to be encoded in bcrypt will be the same" in {
      val userPassword = UserPassword("testUserPassword")
      Future.fromTry(userPassword ?= userPassword.plain)
        .map(assert(_))
    }

    "verification failure possible as UserPassword" in {
      val userPassword = UserPassword("testUserPassword")
      val userInvalidPassword = UserPassword("InvalidUserPassword")
      Future.fromTry(userPassword ?= userInvalidPassword)
        .map(result => assert(!result))
    }

    "verification failure possible as String" in {
      val userPassword = UserPassword("testUserPassword")
      val userInvalidPassword = "InvalidUserPassword"
      Future.fromTry(userPassword ?= userInvalidPassword)
        .map(result => assert(!result))
    }
  }
}
