package com.easycode.akka.http

import scala.concurrent.duration._
import akka.actor.{ Actor, ActorLogging, Props }
import spray.routing.RequestContext
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.actor.ReceiveTimeout

object WorkerMessageProcessActor {
  def props(ctx: RequestContext, msg: String): Props = Props(classOf[WorkerMessageProcessActor], ctx, msg)
}

class WorkerMessageProcessActor(ctx: RequestContext, msg: String) extends Actor with ActorLogging {
  context.actorSelection("/user/workerRouter") ! ConsistentHashableEnvelope(message = msg, hashKey = msg)
  context.setReceiveTimeout(3.second)

  def receive = {
    case msg: String ⇒ {
      println("ready to response: " + msg)
      ctx.complete(msg)
      context.stop(self)
    }
    case ReceiveTimeout ⇒ context.stop(self)
  }
}
