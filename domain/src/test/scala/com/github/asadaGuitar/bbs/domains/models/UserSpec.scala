package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
import scala.concurrent.Await

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

final class UserPasswordSpec extends AnyWordSpec {

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
      assert {
        try {
          val userPassword = UserPassword("6EDI98FE098AP")
          val bcrypted = Await.result(userPassword.bcryptBoundedFuture, Duration.Inf)
          !userPassword.value.equals(bcrypted.value)
        } catch {
          case _: Throwable => false
        }
      }
    }
  }
}
