organization := "org.koderama"

name := "puzzle-exercises"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.9.1"

mainClass := Some("org.koderama.railroad.clients.RailRoadConsoleClient")

scalacOptions += "-deprecation"


// Compile
libraryDependencies ++= {
  Seq(
  )
}

// Provided
libraryDependencies ++= {
  Seq(
  )
}

// Test
libraryDependencies ++= {
  Seq(
    "org.specs2" %% "specs2" % "1.6.1"  withSources(),
    "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test"  withSources(),
    "org.mockito" % "mockito-all" % "1.8.5" % "test" withSources()
  )
}

resolvers ++= Seq(
  "Sonatype OSS" at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Coda Hale's Repository" at "http://repo.codahale.com/"
)
