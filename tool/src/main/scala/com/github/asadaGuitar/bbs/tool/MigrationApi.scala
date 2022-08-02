package com.github.asadaGuitar.bbs.tool

import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.SlickDbConfigProvider
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import slick.migration.api._

object MigrationApi extends SlickDbConfigProvider {
  implicit val dialect: PostgresDialect = new PostgresDialect

  val usersInit =
    TableMigration(Tables.Users).create
      .addColumns(
        _.id,
        _.firstName,
        _.lastName,
        _.emailAddress,
        _.password,
        _.isClose,
        _.createAt,
        _.modifyAt,
        _.closeAt
      )

  val threadsInit =
    TableMigration(Tables.Threads).create
      .addColumns(
        _.id,
        _.userId,
        _.title,
        _.isClose,
        _.createAt,
        _.modifyAt,
        _.closeAt
      )

  val userThreadsInit =
    TableMigration(Tables.UserThreads).create
      .addColumns(_.id, _.userId, _.threadId, _.isClose, _.createAt, _.closeAt)

  val messageInit =
    TableMigration(Tables.Messages).create.addColumns(
      _.id,
      _.threadId,
      _.userId,
      _.text,
      _.isClose,
      _.createAt,
      _.modifyAt,
      _.closeAt
    )

  val migration = usersInit & threadsInit & userThreadsInit & messageInit

  dbConfig.db.run(migration())
}
