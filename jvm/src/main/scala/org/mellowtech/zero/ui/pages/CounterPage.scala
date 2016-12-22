package org.mellowtech.zero.ui.pages

import scalatags.Text.all._
/**
  * @author msvens
  * @since 23/10/16
  */
object CounterPage {

  val boot =
    "org.mellowtech.zero.ui.client.Counter().main()"

  def skeleton(counter: Int) =
    html(
      head(
        script(`type`:="text/javascript", src:="/app/app-fastopt.js"),
        script(`type`:="text/javascript", src:="/app/app-jsdeps.js"),
        link(
          rel:="stylesheet",
          href:="/assets/materializecss/css/materialize.css"
        )
      ),
      body(
        onload:=boot,
        div(id:="content")
      )
    )

}
