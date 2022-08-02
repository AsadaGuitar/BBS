package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
private[adaptors] object Tables
    extends {
      val profile = slick.jdbc.PostgresProfile
    }
    with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this
  * late.) Each generated XXXXTable trait is mixed in this trait hence allowing access to all the TableQuery lazy vals.
  */
private[adaptors] trait Tables extends MessagesTable with ThreadsTable with UsersTable with UserThreadsTable {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Messages.schema ++ Threads.schema ++ Users.schema ++ UserThreads.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

}
