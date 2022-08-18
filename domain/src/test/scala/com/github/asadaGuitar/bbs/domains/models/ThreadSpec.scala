package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class ThreadSpec extends AnyWordSpec {}

final class ThreadIdSpec extends AnyWordSpec {

  "ThreadId" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          ThreadId("6EDI98FE098AP")
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
          ThreadId("");
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}

final class ThreadTitleSpec extends AnyWordSpec {

  "ThreadTitle" should {

    "succeeds when the value is correct." in {
      assert {
        try {
          ThreadTitle("6EDI98FE098AP")
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
          ThreadTitle("")
          false
        }
        catch {
          case _: IllegalArgumentException => true
        }
      }
    }
  }
}
