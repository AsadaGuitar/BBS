package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.flatspec.AnyFlatSpec

final class ThreadSpec extends AnyFlatSpec {}

final class ThreadIdSpec extends AnyFlatSpec {

  "ThreadId" should "succeeds when the value is correct." in {
    assert {
      try { ThreadId("6EDI98FE098AP"); true }
      catch {
        case _: IllegalArgumentException => false
      }
    }
  }

  "ThreadId" should "fails on empty values." in {
    assert {
      try { ThreadId(""); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }
}

final class ThreadTitleSpec extends AnyFlatSpec {

  "ThreadTitle" should "succeeds when the value is correct." in {
    assert {
      try { ThreadTitle("6EDI98FE098AP"); true }
      catch {
        case _: IllegalArgumentException => false
      }
    }
  }

  "ThreadTitle" should "fails on empty values." in {
    assert {
      try { ThreadTitle(""); false }
      catch {
        case _: IllegalArgumentException => true
      }
    }
  }
}
