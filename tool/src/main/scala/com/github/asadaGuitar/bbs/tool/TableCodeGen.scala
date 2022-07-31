package com.github.asadaGuitar.bbs.tool

import slick.codegen.SourceCodeGenerator

object TableCodeGen extends App {

  SourceCodeGenerator.main(
    Array(
      "slick.jdbc.PostgresProfile",
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost:5432/bs_application",
      "com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dto", // package path
      "com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dto", // package name
      "peace", // username
      "", // password
      "true",
      "slick.codegen.SourceCodeGenerator",
      "true"
    )
  )
}
