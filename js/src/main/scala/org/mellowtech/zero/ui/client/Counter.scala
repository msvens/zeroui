package org.mellowtech.zero.ui.client

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactDOM}
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scala.scalajs.js


/**
  * @author msvens
  * @since 23/10/16
  */
object Counter extends JSApp{



  val DaysView = ReactComponentB[Int]("DaysView")
      .render_P(days => {
        <.div(
          ^.className := "col s2",
          <.h1(
            ^.className := "center-align teal-text",
            days
          ),
          <.p(^.className := "center-align","Days")
        )
      }).build

  val HoursView = ReactComponentB[Int]("HoursView")
    .render_P(hours => {
      <.div(
        ^.className := "col s2",
        <.h1(
          ^.className := "center-align teal-text",
          hours
        ),
        <.p(^.className := "center-align","Hours")
      )
    }).build

  val MinutesView = ReactComponentB[Int]("MinutesView")
    .render_P(minutes => {
      <.div(
        ^.className := "col s2",
        <.h1(
          ^.className := "center-align teal-text",
          minutes
        ),
        <.p(^.className := "center-align","Minutes")
      )
    }).build

  val SecondsView = ReactComponentB[Int]("SecondsView")
    .render_P(seconds => {
      <.div(
        ^.className := "col s2",
        <.h1(^.className := "center-align teal-text", seconds),
        <.p(^.className := "center-align","Seconds")
      )
    }).build

  case class CounterState(total: Int, days: Int, hours: Int, minutes: Int, seconds: Int)

  class CounterBackend($: BackendScope[Unit, CounterState]){

    var interval: js.UndefOr[js.timers.SetIntervalHandle] =
      js.undefined

    def breakdownSecs(s: Int): CounterState = {
      var tot = s
      val days: Int = Math.floor(tot / 86400).toInt
      tot = tot % 86400
      val hours: Int = Math.floor(tot / 3600).toInt
      tot = tot % 3600
      val minutes: Int = Math.floor(tot/60).toInt
      val seconds: Int = tot % 60
      CounterState(s,days,hours,minutes,seconds)
    }

    def clear = Callback {
      interval foreach js.timers.clearInterval
      interval = js.undefined
    }

    def tick = {
      $.modState(s => {
        if(s.total == 0) {
          clear
          s
        }
        else
          breakdownSecs(s.total - 1)
      })
    }

    def start = Callback {
      interval = js.timers.setInterval(1000)(tick.runNow())
    }

    def render(s: CounterState) =
      <.div(
        ^.className := "row",
        DaysView(s.days),
        HoursView(s.hours),
        MinutesView(s.minutes),
        SecondsView(s.seconds)
      )

  }

  val CounterView = ReactComponentB[Unit]("CounterView")
    .initialState(CounterState(7200,0,0,0,0))
    .renderBackend[CounterBackend]
    .componentDidMount(_.backend.start)
    .componentWillUnmount(_.backend.clear)
    .build

  def main(): Unit = {
    Console.println("Application starting")
    //log.warn("Application starting")
    // send log messages also to the server
    //log.enableServerLogging("/logging")
    //log.info("This message goes to server as well")

    // create stylesheet
    //GlobalStyles.addToDocument()
    // create the router
    //val router = Router(BaseUrl.until_#, routerConfig)
    // tell React to render the router in the document body
    ReactDOM.render(CounterView(), dom.document.getElementById("content"))
  }


}
