package org.mellowtech.zero.ui.client

import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ReactComponentB, ReactDOM, ReactEventAliases}
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import upickle.default._

import scala.scalajs.js.{Date, JSApp}
import scala.scalajs.js
/**
  * @author msvens
  * @since 13/11/16
  */
object Timer extends JSApp with ReactEventAliases{

  val TimerPreview = ReactComponentB[Int]("TimerPreview")
    .render_P(counter => {
      <.div(
        ^.className := "section",
        <.div(
          ^.className := "row",
          <.embed(
            ^.className := "col s12",
            ^.src := "http://localhost:9020/counters/" + counter
          )
        )
      )
    }).build

  val TimerEmbed = ReactComponentB[Unit]("TimerEmbed")
    .render_P(_ => {
      <.div(
        ^.className := "section",
        <.p("this section will contain the embed to paste")
      )
    }).build

  val TimerTron = ReactComponentB[Unit]("TimerTron")
    .render_P(_ => {
      <.div(
        ^.className := "section row",
        <.div(
          ^.className := "col s12",
          <.div(
            ^.className := "card blue-grey darken-1",
            <.div(
              ^.className := "card-content white-text",
              <.span("Create Your Timer", ^.className := "card-title"),
              <.p("With a few clicks create your own countdown/countup timer. Just fill in the form below")
            )
          )
        )
      )
    }).build

  case class TimerFormState(desc: String = "", title: String = "", startDate: String = "",
                            stopDate: String = "", stopAfter: String = "")

  class TimerFormBackend($: BackendScope[Unit, TimerFormState]){

    def toLocal(d: Date):String = {
      val dl = new Date(d.getTime()-d.getTimezoneOffset()*60000)
      dl.toISOString().substring(0,19)
    }

    def fromLocal(s: String): Date = {
      val d = new Date(s)
      val t = Option(1)

      new Date(d.getTime()+d.getTimezoneOffset()*60000)
    }

    def handleTitleChange(e: ReactEventI) = {
      val t = e.target.value
      $.modState(_.copy(title = t))
    }

    def handleStartChange(e: ReactEventI) = {
      val d = e.target.value
      $.modState(_.copy(startDate = d))
    }

    def handleStopChange(e: ReactEventI) = {
      val sd = e.target.value
      $.modState(_.copy(stopDate = sd))
    }

    def handleStopAfter(e: ReactEventI) = {
      val stop = e.target.value
      $.modState(_.copy(stopAfter = stop))
    }

    def handleDescChange(e: ReactEventI):Callback = {
      val d = e.target.value
      $.modState(_.copy(desc = d))
    }

    def handleSubmit(e: ReactEventI):Callback = {
      e.preventDefault()
      println("in handle submit")
      $.state.map(t => {
        println(t)
      }) >> $.setState(TimerFormState())
    }

    def render(s: TimerFormState) = {
      <.div(^.className := "row",
        <.form(^.className := "col s12", ^.onSubmit ==> handleSubmit,
          <.div(^.className := "row",
            <.div(^.className := "input-field col s12",
              <.input(
                ^.`type` := "text",
                ^.className := "validate",
                ^.id := "inputTitle1",
                ^.value := s.title,
                ^.onChange ==> handleTitleChange
              ),
              <.label( ^.htmlFor := "inputTitle1", "Title")
            )
          ),
          <.div(^.className := "row",
            <.div(^.className := "input-field col s4",
              <.input(
                ^.`type` := "datetime-local",
                ^.className := "validate",
                ^.id := "inputStart1",
                ^.step := "1",
                ^.value := s.startDate,
                ^.onChange ==> handleStartChange
              ),
              <.label(^.className := "active", ^.htmlFor := "inputStart1", "Start")
            )
          ),
          <.div(^.className := "row",
            <.div(^.className := "input-field col s4",
              <.input(
                ^.`type` := "datetime-local",
                ^.className := "validate",
                ^.id := "inputStop1",
                ^.step := "1",
                ^.value := s.stopDate,
                ^.onChange ==> handleStopChange
              ),
              <.label(^.className := "active", ^.htmlFor := "inputStop1", "Stop")
            )
          ),
          <.div(^.className := "row",
            <.div(^.className := "input-field col s4",
              <.input(
                ^.`type` := "number",
                ^.className := "validate",
                ^.id := "inputStopAfter1",
                ^.value := s.stopAfter,
                ^.onChange ==> handleStopAfter
              ),
              <.label(^.htmlFor := "inputStopAfter1", "Stop in secs")
            )
          ),
          <.div(^.className := "row",
            <.div(^.className := "input-field col s12",
              <.textarea(
                ^.className := "materialize-textarea",
                ^.id := "inputDesc1",
                ^.rows := "3",
                ^.value := s.desc,
                ^.onChange ==> handleDescChange
              ),
              <.label(^.htmlFor := "inputDesc1", "Description")
            )
          ),
          <.div(^.className := "row",
            <.div(^.className := "input-field col s3",
              <.button(
                ^.className := "btn waves-effect waves-light",
                ^.`type` := "submit",
                ^.name := "action",
                "Submit",
                <.i(^.className := "material-icons right", "send")
              )
            )
          )
        )
      )
    }

  }

  val TimerFormView = ReactComponentB[Unit]("TimerFormView")
    .initialState(TimerFormState())
    .renderBackend[TimerFormBackend]
    .build

  case class TimerPanelProps(url: String, tickInterval: Int)

  class TimerPanelBackend($: BackendScope[TimerPanelProps, Unit]){
    def handleTimerSubmit(t: TimerFormState): CallbackTo[Unit] = {
      $.props.map{p => {
          val u = p.url
          val data = write(t)
          Console.println(data)
        }
      }
    }

    def render() = {
      <.div(
        ^.className := "timerPanel",
        TimerTron(),
        TimerFormView(),
        TimerPreview(15),
        TimerEmbed()
      )
    }
  }

  val TimerPanel = ReactComponentB[TimerPanelProps]("TimerPanel")
    .backend(new TimerPanelBackend(_))
    .renderBackend
    .build


  def main(): Unit = {
    Console.println("Application starting")
    val p = TimerPanelProps("someurl", 2000)
    ReactDOM.render(TimerPanel(p), dom.document.getElementById("content"))
  }

}
