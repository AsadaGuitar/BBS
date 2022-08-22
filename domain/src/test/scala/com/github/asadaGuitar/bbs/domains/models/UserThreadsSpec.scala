package com.github.asadaGuitar.bbs.domains.models

import org.scalatest.wordspec.AnyWordSpec

import scala.Double.NaN

final class UserThreadsSpec extends AnyWordSpec {}

final class UserThreadsIdSpec extends AnyWordSpec {

  "UserThreadsId.apply" should {

    "can insert 0 or NaN." in {
      assert {
        UserThreadsId(0)
        true
      }
      assert {
        UserThreadsId(NaN.toInt)
        true
      }
    }
  }
}
