
name := """ddd"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += evolutions
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += specs2 % Test

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.0",
  "org.scalikejdbc" %% "scalikejdbc-test" % "3.3.0" % "test",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.3",
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.6.0-scalikejdbc-3.3"
)

com.typesafe.sbt.SbtScalariform.ScalariformKeys.preferences := {
  import scalariform.formatter.preferences._
  FormattingPreferences()
    .setPreference(AlignArguments, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 20)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(CompactControlReadability, true)
    .setPreference(SpacesAroundMultiImports, false)
}