package com.easycode.akka.http

import akka.actor.{ ActorSystem, Props }
import com.typesafe.config.ConfigFactory

object Main extends App {
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

  implicit val system = ActorSystem("Computation", config)

  // val handler = system.actorOf(Props[FrontEndActor], name = "front-end")
}
