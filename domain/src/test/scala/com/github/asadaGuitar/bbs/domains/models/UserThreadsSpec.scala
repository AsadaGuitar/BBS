package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

import scala.Double.NaN

final class UserThreadsSpec extends AnyWordSpec {}

final class UserThreadsIdSpec extends AnyWordSpec {

  "UserThreadsId.apply" should {

    "Nothing when the value is 0 or NaN." in {
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

    "if the value is not 0 or NaN, it is Just." in {
      assert {
        UserThreadsId(100) match {
          case UserThreadsId.Just(value) => value == 100
          case UserThreadsId.Nothing => false
        }
      }
    }
  }
}
