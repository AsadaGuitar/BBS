package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

private[adaptors] object Tables
    extends {
      val profile = slick.jdbc.PostgresProfile
    }
    with Tables

private[adaptors] trait Tables extends MessagesTable with ThreadsTable with UsersTable with UserThreadsTable {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._

  lazy val schema: profile.SchemaDescription = Messages.schema ++ Threads.schema ++ Users.schema ++ UserThreads.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl: profile.DDL = schema

}
