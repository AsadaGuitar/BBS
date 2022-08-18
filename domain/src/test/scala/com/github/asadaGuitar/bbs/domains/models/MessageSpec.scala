package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class MessageSpec extends AnyWordSpec {}

final class MessageIdSpec extends AnyWordSpec {

  "MessageId" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          MessageId("B4XO9FEO4FIO")
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
          MessageId("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}

final class MessageTextSpec extends AnyWordSpec {

  "MessageText" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          MessageText("test")
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
          MessageText("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}
