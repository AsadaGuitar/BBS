package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

final class MessageSpec extends AnyWordSpec {}

final class MessageIdSpec extends AnyWordSpec {

  "MessageId.apply" should {

    "succeeds when the value length is 12." in {
      assert {
        MessageId(Utils.generateRandomString(12))
        true
      }
    }

    "fails on the value is more shorter than length required." in {
      assertThrows[IllegalArgumentException] {
        MessageId(Utils.generateRandomString(11))
      }
    }

    "fails on the value is more longer then length required." in {
      assertThrows[IllegalArgumentException] {
        MessageId(Utils.generateRandomString(13))
      }
    }

    "fails on empty values." in {
      assertThrows[IllegalArgumentException] {
        MessageId("")
      }
    }
  }
}

final class MessageTextSpec extends AnyWordSpec {

  "MessageText.apply" should {

    "succeeds on the value length is 1." in {
      assert {
        MessageText(Utils.generateRandomString(1))
        true
      }
    }

    "succeeds on the value length is 1024." in {
      assert {
        MessageText(Utils.generateRandomString(1024))
        true
      }
    }

    "succeeds on the value length is 512." in {
      assert {
        MessageText(Utils.generateRandomString(512))
        true
      }
    }

    "fails on the value is empty" in {
      assertThrows[IllegalArgumentException] {
        MessageText("")
      }
    }

    "fails on the value length is more longer than length required." in {
      assertThrows[IllegalArgumentException] {
        MessageText(Utils.generateRandomString(1025))
      }
    }
  }
}
