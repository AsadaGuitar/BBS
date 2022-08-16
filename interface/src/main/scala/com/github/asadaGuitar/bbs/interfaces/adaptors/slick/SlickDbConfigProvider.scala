package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import java.sql.Timestamp
import java.time.Instant

trait SlickDbConfigProvider {

  protected val dbConfig: DatabaseConfig[PostgresProfile] = DatabaseConfig.forConfig("slick")

  implicit val convertTimestampToInstant: Timestamp => Instant =
    timestamp => Instant.ofEpochMilli(timestamp.getTime)

  implicit val convertInstantToTimestamp: Instant => Timestamp =
    instant => new Timestamp(instant.toEpochMilli)
}
