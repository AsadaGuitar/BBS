package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

trait SlickDbConfigProvider {

  protected val dbConfig: DatabaseConfig[PostgresProfile] = DatabaseConfig.forConfig("slick")

}
