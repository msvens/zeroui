## Overview

Zeroui is mainly a playground project for a combined scalajs+react+akka http+materializecss
application using sbt-web and sbt-revolver for simplified development. The aim is to turn
it to a useful template for myself and other to use.

The project setup is based on David Hewitt's excellent [scalajs-template](https://github.com/davidhewitt/scalajs-template).
Thanks for this, it is the best template Ive found!

###Goals

For some time now I've felt that play is a little bit too big for most things I want to build - especially for
non-ui services. This has lead me to explore akka-http. Im also very curious about scalajs and want to be able to
use that to build react based applications. Finally, I've grown tired of Bootstrap and is eager to try out materializecss.

I would like to quickly (in a matter of minutes) create a new project that uses the above components.


###sbt-revolver

To try it out, open sbt and run:

    sbt> re-start

To close the web process, instead run:

    sbt> re-stop


###Using as a template

I've yet to break out and document the pieces but in time I will. However, don't be afraid of using this
project as a template (you just have to remove the stuff you dont need)