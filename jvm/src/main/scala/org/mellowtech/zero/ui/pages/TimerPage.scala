package org.mellowtech.zero.ui.pages

import scalatags.Text.all._

/**
  * @author msvens
  * @since 23/10/16
  */
object TimerPage {

  val boot =
    "org.mellowtech.zero.ui.client.Timer().main()"

  val skeleton =
    html(
      head(
        script(`type`:="text/javascript", src:="/app/app-fastopt.js"),
        script(`type`:="text/javascript", src:="/app/app-jsdeps.js"),
        link(
          rel:="stylesheet",
          href:="/assets/materializecss/css/materialize.css"
        ),
        link(
          href:="http://fonts.googleapis.com/icon?family=Material+Icons",
          rel:="stylesheet"
        )
      ),
      body(
        onload:=boot,
        div(id:="content")
      )
    )

}
