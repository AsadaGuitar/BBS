package com.github.asadaGuitar.bbs.interfaces.controllers

import com.typesafe.config.{Config, ConfigFactory}

trait MessageProvider {

  protected val config: Config = ConfigFactory.load("message")
}
