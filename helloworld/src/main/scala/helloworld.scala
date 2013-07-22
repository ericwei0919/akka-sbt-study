package com.easycode.akka

import akka.actor.{ Actor, ActorSystem, ActorLogging, Props }

case object Start
case class Hello(name: String)

class HelloActor extends Actor with ActorLogging {
  val worldActor = context.actorOf(Props[WorldActor], name = "world")

  def receive = {
    case Start ⇒ worldActor ! "Hello"
    case Hello(name) ⇒
      log.info("Recieved message [{}]", "Hello, " + name);
    case message: String ⇒
      log.info("Recieved message [{}]", message);
  }
}

class WorldActor extends Actor {
  def receive = {
    case message: String ⇒ sender ! (message.toUpperCase + " world")
  }
}

object HelloWorld {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem()
    val helloActor = system.actorOf(Props[HelloActor])
    helloActor ! Start
    helloActor ! Hello("Lucifer")
  }
}