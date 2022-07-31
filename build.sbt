ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val akkaVersion               = "2.6.19"
val akkaHttpVersion           = "10.2.9"
val jwtAkkaHttpVersion        = "1.4.4"
val catsCoreVersion           = "2.7.0"
val slickVersion              = "3.3.3"
val slickMigrationVersion     = "0.8.2"
val logbackVersion            = "1.2.11"
val postgresVersion           = "42.3.6"
val scalaTestVersion          = "3.2.12"


lazy val domain = (project in file("domain"))
  .settings(
    name := "bbs-domain",
    libraryDependencies ++= Seq(
      "org.scalatest"                        %% "scalatest"                      % scalaTestVersion       % "test"
    )
  )

lazy val repository = (project in file("repository"))
  .settings(
    name := "bbs-repository",
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions"
    ),
    libraryDependencies ++= Seq(
      "org.typelevel"                        %% "cats-core"                      % catsCoreVersion,
      "com.typesafe.slick"                   %% "slick"                          % slickVersion,
      "com.typesafe.slick"                   %% "slick-hikaricp"                 % slickVersion,
      "org.postgresql"                       %  "postgresql"                     % postgresVersion,
      "ch.qos.logback"                       %  "logback-classic"                % logbackVersion,
      "org.scalatest"                        %% "scalatest"                      % scalaTestVersion       % "test"
    )
  )
  .dependsOn(domain)

lazy val usecase = (project in file("use-case"))
  .settings(
    name := "bbs-use-case",
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions"
    ),
    libraryDependencies ++= Seq(
      "org.typelevel"                        %% "cats-core"                      % catsCoreVersion,
      "ch.qos.logback"                       %  "logback-classic"                % logbackVersion,
      "org.scalatest"                        %% "scalatest"                      % scalaTestVersion       % "test"
    )
  )
  .dependsOn(repository)

lazy val interface = (project in file("interface"))
  .settings(
    name := "bbs-interface",
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions"
    ),
    libraryDependencies ++= Seq(
      "com.typesafe.akka"                    %% "akka-actor-typed"               % akkaVersion,
      "com.typesafe.akka"                    %% "akka-http-spray-json"           % akkaHttpVersion,
      "com.typesafe.akka"                    %% "akka-http"                      % akkaHttpVersion,
      "com.emarsys"                          %% "jwt-akka-http"                 % jwtAkkaHttpVersion,
      "org.typelevel"                        %% "cats-core"                      % catsCoreVersion,
      "com.typesafe.slick"                   %% "slick"                          % slickVersion,
      "com.typesafe.slick"                   %% "slick-hikaricp"                 % slickVersion,
      "org.postgresql"                       %  "postgresql"                     % postgresVersion,
      "com.typesafe.akka"                    %% "akka-slf4j"                     % akkaVersion,
      "ch.qos.logback"                       %  "logback-classic"                % logbackVersion,
      "com.typesafe.akka"                    %% "akka-testkit"                   % akkaVersion            % "test",
      "org.scalatest"                        %% "scalatest"                      % scalaTestVersion       % "test"
    )
  )
  .dependsOn(usecase)

lazy val api = (project in file("api"))
  .settings(
    name := "bbs-api",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"                    %% "akka-actor-typed"               % akkaVersion,
      "com.typesafe.akka"                    %% "akka-http"                      % akkaHttpVersion,
      "org.typelevel"                        %% "cats-core"                      % catsCoreVersion,
      "com.typesafe.akka"                    %% "akka-slf4j"                     % akkaVersion,
      "com.typesafe.akka"                    %% "akka-stream"                    % akkaVersion,
      "ch.qos.logback"                       %  "logback-classic"                % logbackVersion,
      "com.typesafe.akka"                    %% "akka-testkit"                   % akkaVersion            % "test",
      "org.scalatest"                        %% "scalatest"                      % scalaTestVersion       % "test"
    )
  )
  .dependsOn(interface)


lazy val codegen = taskKey[Unit]("generate slick table code")
lazy val tool = (project in file("tool"))
  .settings(
    name := "bbs-tool",
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions"
    ),
    libraryDependencies ++= Seq(
      "com.typesafe.slick"                   %% "slick"                          % slickVersion,
      "com.typesafe.slick"                   %% "slick-hikaricp"                 % slickVersion,
      "org.postgresql"                       %  "postgresql"                     % postgresVersion,
      "com.typesafe.slick"                   %% "slick-codegen"                  % slickVersion,
      "io.github.nafg.slick-migration-api"   %% "slick-migration-api"            % slickMigrationVersion,
      "ch.qos.logback"                       %  "logback-classic"                % logbackVersion,
      "org.scalatest"                        %% "scalatest"                      % scalaTestVersion       % "test"
    )
  )
