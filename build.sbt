name := "zeroui"

import com.typesafe.sbt.web.SbtWeb
import SbtWeb.autoImport.WebKeys._

lazy val root = project.in(file(".")).
  aggregate(appJS, appJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val app = crossProject.in(file(".")).
  settings(
    name := "app",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % "0.6.2",
      "com.lihaoyi" %%% "upickle" % "0.4.3",
      "com.lihaoyi" %%% "autowire" % "0.2.6"
    ),
    scalacOptions ++= Seq("-feature")
  ).
  jvmConfigure(
    (jvmProject: Project) => {
      jvmProject.copy(settings = (Revolver.settings ++ jvmProject.settings))
    }
  ).
  jvmSettings(
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-json4s" % "1.10.1",
      "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.11",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "org.webjars" % "materializecss" % "0.97.7"
    ),
    baseDirectory in Revolver.reStart := file("./"),
    // unmanagedResourceDirectories in Compile += file("./") / "bower_components",
    webTarget := (classDirectory in Compile).value / "web",
    Revolver.reStart <<= Revolver.reStart.dependsOn(assets)
  ).
  jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.github.japgolly.scalajs-react" %%% "core" % "0.11.3",
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
    ),
    jsDependencies += "org.webjars" % "jquery" % "2.1.4" / "jquery.js",
    jsDependencies += "org.webjars" % "materializecss" % "0.97.7" / "materialize.js" minified "materialize.min.js" dependsOn "jquery.js",
    jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.3.2" / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
      "org.webjars.bower" % "react" % "15.3.2" / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
      "org.webjars.bower" % "react" % "15.3.2" / "react-dom-server.js" minified  "react-dom-server.min.js" dependsOn "react-dom.js" commonJSName "ReactDOMServer")
  ).enablePlugins(SbtWeb)

lazy val appJVM = app.jvm.settings(
  (resources in Compile) ++= Seq(
    (fastOptJS in (appJS, Compile)).value.data,
    (packageJSDependencies in (appJS, Compile)).value
  )
)

lazy val appJS = app.js
