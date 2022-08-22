ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)

val akkaVersion        = "2.6.19"
val akkaHttpVersion    = "10.2.9"
val jwtAkkaHttpVersion = "1.4.4"
val catsCoreVersion    = "2.8.0"
val slickVersion       = "3.3.3"
val logbackVersion     = "1.2.11"
val postgresVersion    = "42.3.6"
val scalaTestVersion   = "3.2.12"

lazy val baseSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.13.8",
  scalacOptions ++= (
    Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-encoding",
      "UTF-8",
      "-language:_",
      "-Ydelambdafy:method",
      "-target:jvm-1.8",
      "-Yrangepos",
      "-Ywarn-unused"
    )
  ),
  resolvers ++= Resolver.sonatypeOssRepos("snapshots") ++ Resolver.sonatypeOssRepos("releases") ++ Seq(
    "Seasar Repository" at "https://maven.seasar.org/maven2/",
    "DynamoDB Local Repository" at "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"
  ),
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  Test / publishArtifact := false,
  Test / fork := true,
  Test / parallelExecution := false
)

lazy val domain = (project in file("domain"))
  .settings(baseSettings)
  .settings(
    name := "bbs-domain",
    libraryDependencies ++= Seq(
      "com.github.t3hnar" %% "scala-bcrypt"      % "4.3.0",
      "org.typelevel"     %% "cats-core"         % catsCoreVersion,
      "commons-validator"  % "commons-validator" % "1.7",
      "org.scalatest"     %% "scalatest"         % scalaTestVersion % Test
    )
  )

lazy val repository = (project in file("repository"))
  .settings(baseSettings)
  .settings(
    name := "bbs-repository",
    libraryDependencies ++= Seq(
      "org.typelevel"      %% "cats-core"       % catsCoreVersion,
      "com.typesafe.slick" %% "slick"           % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp"  % slickVersion,
      "org.postgresql"      % "postgresql"      % postgresVersion,
      "ch.qos.logback"      % "logback-classic" % logbackVersion,
      "org.scalatest"      %% "scalatest"       % scalaTestVersion % Test
    )
  )
  .dependsOn(domain)

lazy val usecase = (project in file("use-case"))
  .settings(baseSettings)
  .settings(
    name := "bbs-use-case",
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-core"       % catsCoreVersion,
      "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
      "ch.qos.logback"     % "logback-classic" % logbackVersion,
      "org.scalatest"     %% "scalatest"       % scalaTestVersion % Test
    )
  )
  .dependsOn(repository)

lazy val interface = (project in file("interface"))
  .settings(baseSettings)
  .settings(
    name := "bbs-interface",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"                  %% "akka-actor-typed"     % akkaVersion,
      "com.typesafe.akka"                  %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka"                  %% "akka-http"            % akkaHttpVersion,
      "com.emarsys"                        %% "jwt-akka-http"        % jwtAkkaHttpVersion,
      "org.typelevel"                      %% "cats-core"            % catsCoreVersion,
      "com.typesafe.slick"                 %% "slick"                % slickVersion,
      "com.typesafe.slick"                 %% "slick-hikaricp"       % slickVersion,
      "io.github.nafg.slick-migration-api" %% "slick-migration-api"  % "0.8.2",
      "org.postgresql"                      % "postgresql"           % postgresVersion,
      "com.typesafe.akka"                  %% "akka-slf4j"           % akkaVersion,
      "ch.qos.logback"                      % "logback-classic"      % logbackVersion,
      "com.typesafe.akka"                  %% "akka-testkit"         % akkaVersion      % Test,
      "org.scalatest"                      %% "scalatest"            % scalaTestVersion % Test
    )
  )
  .dependsOn(usecase)

lazy val api = (project in file("api"))
  .settings(baseSettings)
  .settings(
    name := "bbs-api",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-http"        % akkaHttpVersion,
      "org.typelevel"     %% "cats-core"        % catsCoreVersion,
      "com.typesafe.akka" %% "akka-slf4j"       % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"      % akkaVersion,
      "ch.qos.logback"     % "logback-classic"  % logbackVersion,
      "com.typesafe.akka" %% "akka-testkit"     % akkaVersion      % Test,
      "org.scalatest"     %% "scalatest"        % scalaTestVersion % Test
    )
  )
  .dependsOn(interface)

lazy val codegen = taskKey[Unit]("generate slick table code")
lazy val tool = (project in file("tool"))
  .settings(baseSettings)
  .settings(
    name := "bbs-tool",
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick"           % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp"  % slickVersion,
      "org.postgresql"      % "postgresql"      % postgresVersion,
      "com.typesafe.slick" %% "slick-codegen"   % slickVersion,
      "ch.qos.logback"      % "logback-classic" % logbackVersion,
      "org.scalatest"      %% "scalatest"       % scalaTestVersion % Test
    )
  ).dependsOn(interface)

lazy val root = (project in file("."))
  .settings(baseSettings)
  .settings(
    name := "bbs-root",
    publish / skip := true
  )
  .aggregate(domain, interface, repository, api)

addCommandAlias("lint", ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck;scalafixAll --check")
addCommandAlias("fmt", ";scalafmtAll;scalafmtSbt;scalafix RemoveUnused")
