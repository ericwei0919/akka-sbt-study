package com.easycode.akka

import sbt._
import sbt.Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
import akka.sbt.AkkaKernelPlugin
import akka.sbt.AkkaKernelPlugin.{ Dist, outputDirectory, distJvmOptions }

object AkkaStudy extends Build {
  EclipseKeys.skipParents in ThisBuild := false

  lazy val buildSettings = Seq(
    organization := "com.easycode",
    version := "0.1",
    scalaVersion := "2.10.2",
    scalaBinaryVersion <<= (scalaVersion, scalaBinaryVersion)((v, bv) ⇒ System.getProperty("akka.scalaBinaryVersion", if (v contains "-") v else bv)))

  override lazy val settings =
    super.settings ++
      buildSettings ++
      Seq(
        shellPrompt := { s ⇒ Project.extract(s).currentProject.id + " > " })

  lazy val baseSettings = Defaults.defaultSettings

  lazy val parentSettings = baseSettings ++ Seq(
    publishArtifact := false)

  lazy val defaultSettings = baseSettings ++ Seq(
    // compile options
    scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.6", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
    javacOptions in Compile ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6", "-Xlint:unchecked", "-Xlint:deprecation"),

    // if changing this between binary and full, also change at the bottom of akka-sbt-plugin/sample/project/Build.scala
    crossVersion := CrossVersion.binary,

    // ivyLoggingLevel in ThisBuild := UpdateLogging.Quiet,
    resolvers ++= Seq(
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "spray repo" at "http://nightlies.spray.io/"))

  lazy val study = Project(
    id = "akka-study",
    base = file("."),
    settings = parentSettings,
    aggregate = Seq(helloworld))

  lazy val helloworld = Project(
    id = "helloworld",
    base = file("helloworld"),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Dependencies.helloworld))

  lazy val simple = Project(
    id = "simple",
    base = file("simple"),
    settings = defaultSettings ++ AkkaKernelPlugin.distSettings ++ Seq(
      libraryDependencies ++= Dependencies.kernel,
      outputDirectory in Dist := file("target/simple-dist"))) dependsOn (frontend, backend)

  lazy val frontend = Project(
    id = "frontend",
    base = file("frontend"),
    settings = defaultSettings ++ Seq(
      // javaOptions in run ++= Seq(
      //   "-Djava.library.path=../sigar"),
      // "-Xms128m", "-Xmx512m"),
      // Keys.fork in run := true,
      libraryDependencies ++= Dependencies.frontend))

  lazy val backend = Project(
    id = "backend",
    base = file("backend"),
    settings = defaultSettings ++ Seq(
      // javaOptions in run ++= Seq(
      //   "-Djava.library.path=../sigar"),
      // "-Xms128m", "-Xmx512m"),
      // Keys.fork in run := true,
      libraryDependencies ++= Dependencies.backend))
}

object Dependencies {

  object Compile {
    val actor = "com.typesafe.akka" %% "akka-actor" % "2.2.0" // ApacheV2
    val cluster = "com.typesafe.akka" %% "akka-cluster" % "2.2.0" // ApacheV2
    val akkaKernel = "com.typesafe.akka" %% "akka-kernel" % "2.2.0" // ApacheV2
    val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.2.0" // ApacheV2
    val config = "com.typesafe" % "config" % "1.0.2" // ApacheV2
    val netty = "io.netty" % "netty" % "3.6.6.Final" // ApacheV2
    val protobuf = "com.google.protobuf" % "protobuf-java" % "2.4.1" // New BSD

    val sprayCan = "io.spray" % "spray-can" % "1.2-20130719"
    val sprayRouting = "io.spray" % "spray-routing" % "1.2-20130719"

    val sigar = "org.fusesource" % "sigar" % "1.6.4" exclude ("log4j", "log4j") // ApacheV2

    val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.2" // MIT
    val log4jslf4j = "org.slf4j" % "log4j-over-slf4j" % "1.7.2" // MIT
    val logback = "ch.qos.logback" % "logback-classic" % "1.0.7" // EPL 1.0 / LGPL 2.1

    // Test

    object Test {
      val junit = "junit" % "junit" % "4.10" % "test" // Common Public License 1.0
      val mockito = "org.mockito" % "mockito-all" % "1.8.1" % "test" // MIT
      // changing the scalatest dependency must be reflected in akka-docs/rst/dev/multi-jvm-testing.rst
      val scalatest = "org.scalatest" %% "scalatest" % "1.9.1" % "test" // ApacheV2
      val scalacheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" // New BSD
      val junitIntf = "com.novocode" % "junit-interface" % "0.8" % "test" // MIT
    }
  }

  import Compile._

  val helloworld = Seq(actor, akkaSlf4j, logback)
  val frontend = Seq(actor, cluster, config, sprayCan, sprayRouting, akkaSlf4j, logback, sigar, log4jslf4j)
  val backend = Seq(actor, cluster, config, akkaSlf4j, logback, sigar, log4jslf4j)
  val kernel = Seq(akkaKernel, akkaSlf4j, logback)
}
