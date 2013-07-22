package com.easycode.akka.http

import scala.concurrent.duration._
import akka.actor.{ Actor, Props }
import spray.routing._
import spray.util._
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope

class FrontEndActor extends Actor with FrontEndService {
  def actorRefFactory = context

  def receive = runRoute(route)
}

trait FrontEndService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher

  def route = {
    path("hello" / Segment) { name ⇒
      get {
        complete {
          println("/hello/" + name)
          "Hello, " + name
        }
      }
    } ~
      path("ping") {
        get {
          println("ping")
          complete("PONG")
        }
      } ~
      path("work" / Segment) { msg ⇒
        get { ctx ⇒
          actorRefFactory.actorOf(WorkerMessageProcessActor.props(ctx, msg))
        }
      } ~
      path("stop") {
        get {
          complete {
            println("stopping actor system...")
            in(1.second) { actorSystem.shutdown() }
            "Shutting down in 1 second..."
          }
        }
      }
  }
  def in[U](duration: FiniteDuration)(body: ⇒ U): Unit =
    actorSystem.scheduler.scheduleOnce(duration)(body)
}
