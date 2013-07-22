package com.easycode.akka.http

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import spray.can.server.ServerSettings
import com.typesafe.config.ConfigFactory

object Main extends App {
  println(System.getProperty("config"))

  var config = ConfigFactory.load().getConfig(
    Option(System.getProperty("config")) match {
      case Some(config) ⇒ config
      case _            ⇒ "standalone"
    });

  config = Option(System.getProperty("akka.port")) match {
    case Some(port) ⇒ {
      ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(config);
    }
    case _ ⇒ config
  }

  val httpPort = Option(System.getProperty("http.port")) match {
    case Some(port) ⇒ port.toInt
    case _          ⇒ 8081
  }

  implicit val system = ActorSystem("Computation", config)

  // the handler actor replies to incoming HttpRequests
  val handler = system.actorOf(Props[FrontEndActor], name = "front-end")

  IO(Http) ! Http.Bind(handler, interface = "localhost", port = httpPort, settings = Some(ServerSettings(system)))
}
