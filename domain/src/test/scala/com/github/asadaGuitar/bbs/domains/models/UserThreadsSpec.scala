package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.flatspec.AnyFlatSpec

import scala.Double.NaN

final class UserThreadsSpec extends AnyFlatSpec {}

final class UserThreadsIdSpec extends AnyFlatSpec {

  "UserThreadsId.apply" should "Nothing when the value is 0 or NaN." in {
    assert {
      UserThreadsId(0) match {
        case UserThreadsId.Just(_) => false
        case UserThreadsId.Nothing => true
      }
    }
    assert {
      UserThreadsId(NaN.toInt) match {
        case UserThreadsId.Just(_) => false
        case UserThreadsId.Nothing => true
      }
    }
  }

  "UserThreadsId.apply" should "if the value is not 0 or NaN, it is Just." in {
    assert {
      UserThreadsId(100) match {
        case UserThreadsId.Just(value) => value == 100
        case UserThreadsId.Nothing     => false
      }
    }
  }
}
