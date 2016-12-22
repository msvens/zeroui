package org.mellowtech.zero.ui

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, PathMatchers}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import org.mellowtech.zero.ui.pages.{CounterPage, TimerPage}

import scala.concurrent.ExecutionContext

/*import akka.actor.ActorSystem
import spray.http.{HttpEntity, MediaTypes}
import spray.routing.SimpleRoutingApp
import scala.concurrent.ExecutionContext.Implicits.global
import autowire._

import scala.util.Properties
*/


trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")

  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
}

object Server extends Config {

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    Http().bindAndHandle(route, httpHost, httpPort)
  }

  def route(implicit m: Materializer) = {
    import Directives._

    pathPrefix("counters") {
      log.debug("found timers path...")
      path(IntNumber) { id =>
        get {
          val r = CounterPage.skeleton(id).render
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, r))
        }
      }
    } ~
    path("timer") {
      get {
        val t = TimerPage.skeleton.render
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, t))
      }
    } ~
    pathPrefix("app") {
      getFromResourceDirectory("")
    } ~
    pathPrefix("assets") {
      getFromResourceDirectory("web/public/main/lib")
    }

  }
}


/*
object Router extends autowire.Server[String, upickle.Reader, upickle.Writer]{
  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}

object Server extends SimpleRoutingApp {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    startServer("0.0.0.0", port = 8080){
      pathSingleSlash{
        get{
          complete{
            HttpEntity(
              MediaTypes.`text/html`,
              Page.skeleton.render
            )
          }
        }
      } ~
      pathPrefix("app") {
        getFromResourceDirectory("")
      } ~
      pathPrefix("assets") {
        getFromResourceDirectory("web/public/main/lib/")
      } ~
      path("ajax" / Segments){ s =>
        post{
          extract(_.request.entity.asString) { e =>
            complete {
              Router.route[Api](ApiImpl)(
                autowire.Core.Request(
                  s,
                  upickle.read[Map[String, String]](e)
                )
              )
            }
          }
        }
      }
    }
  }
}
*/