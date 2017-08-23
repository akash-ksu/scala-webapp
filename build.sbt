import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import ScalateKeys._


val ScalatraVersion = "2.5.1"

val elastic4sVersion = "5.4.0"

ScalatraPlugin.scalatraSettings

scalateSettings

organization := "com.egen"

name := "event-source-core"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.3"

resolvers += Classpaths.typesafeReleases

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

mainClass in assembly := Some("com.egen.app.Launcher")
libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.5.0",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % "2.8.4",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-smile" % "2.8.4",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.8.4",
  "org.apache.kafka" % "kafka-clients" % "0.11.0.0",
  "org.scala-lang" % "scala-library" % ScalatraVersion % "runtime"
)

libraryDependencies +=
  "org.elasticsearch" % "elasticsearch" % "2.3.5" excludeAll(ExclusionRule ( organization = "com.fasterxml.jackson.dataformat"))

scalateTemplateConfig in Compile := {
  val base = (sourceDirectory in Compile).value
  Seq(
    TemplateConfig(
      base / "webapp" / "WEB-INF" / "templates",
      Seq.empty,  /* default imports should be added here */
      Seq(
        Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
      ),  /* add extra bindings here */
      Some("templates")
    )
  )
}

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

enablePlugins(JettyPlugin)
