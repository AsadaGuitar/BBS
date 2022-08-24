package com.github.asadaGuitar.bbs.usecases

import scala.util.Random

object Utils {

  def generateRandomString(length: Int): String =  Random.alphanumeric.take(length).mkString

}
