package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class ThreadSpec extends AnyWordSpec {}

final class ThreadIdSpec extends AnyWordSpec {

  "ThreadId.apply" should {

    "succeeds when the value length is 12." in {
      assert {
        ThreadId(Utils.generateRandomString(12))
        true
      }
    }

    "fails on the value is shorter required length." in {
      assertThrows[IllegalArgumentException] {
        ThreadId(Utils.generateRandomString(11))
      }
    }

    "fails on the value is longer required length." in {
      assertThrows[IllegalArgumentException] {
        ThreadId(Utils.generateRandomString(13))
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        ThreadId("")
      }
    }
  }

  "ThreadId.matches" should {

    "succeeds when the value length is 12." in {
      assert(ThreadId.matches(Utils.generateRandomString(12)))
    }

    "fails on the value is shorter required length." in {
      assert(!ThreadId.matches(Utils.generateRandomString(11)))
    }

    "fails on the value is longer required length." in {
      assert(!ThreadId.matches(Utils.generateRandomString(13)))
    }

    "fails on empty values." in {
      assert(!ThreadId.matches(""))
    }
  }
}

final class ThreadTitleSpec extends AnyWordSpec {

  "ThreadTitle.apply" should {

    "succeeds when the value length is 8." in {
      assert {
        ThreadTitle(Utils.generateRandomString(8))
        true
      }
    }

    "succeeds when the value length is 255." in {
      assert {
        ThreadTitle(Utils.generateRandomString(255))
        true
      }
    }

    "succeeds when the value length is 127." in {
      assert {
        ThreadTitle(Utils.generateRandomString(127))
        true
      }
    }

    "fails on the value is shorter required length." in {
      assert(!ThreadId.matches(Utils.generateRandomString(7)))
    }

    "fails on the value is longer required length." in {
      assert(!ThreadId.matches(Utils.generateRandomString(256)))
    }

    "fails on empty values." in {
      assert(!ThreadId.matches(""))
    }
  }
}
