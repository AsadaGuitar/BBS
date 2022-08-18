package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.flatspec.AnyFlatSpec

final class MessageSpec extends AnyFlatSpec {}

final class MessageIdSpec extends AnyFlatSpec {

  "MessageId" should "succeeds when the value is correct." in {
    assert {
      try { MessageId("B4XO9FEO4FIO"); true }
      catch {
        case _: IllegalArgumentException => false
      }
    }
  }

  "MessageId" should "fails on empty values." in {
    assert {
      try { MessageId(""); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }
}

final class MessageTextSpec extends AnyFlatSpec {

  "MessageText" should "succeeds when the value is correct." in {
    assert {
      try { MessageText("test"); true }
      catch {
        case _: IllegalArgumentException => false
      }
    }
  }

  "MessageText" should "fails on empty values." in {
    assert {
      try { MessageText(""); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }
}
