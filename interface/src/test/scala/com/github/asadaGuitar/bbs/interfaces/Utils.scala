package com.github.asadaGuitar.bbs.interfaces

import scala.util.Random

object Utils {

  def generateRandomString(length: Int): String =  Random.alphanumeric.take(length).mkString
}
