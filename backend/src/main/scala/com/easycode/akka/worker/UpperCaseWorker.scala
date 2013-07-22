package com.easycode.akka.worker

import akka.actor.{Actor, ActorLogging}

class UpperCaseActor extends Actor with ActorLogging {
  def receive = {
    case str: String ⇒ {
      println("received: " + str)
      sender ! str.toUpperCase
    }
  }
}