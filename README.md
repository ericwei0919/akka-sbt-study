akka-sbt-study
==============

setup project environment:
* Install sbt: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html
* in project home folder, start sbt and run following command
eclipse with-source=true
* open eclipse, import all projects

Run front-end cluster app:
* Seed node 1:
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2551 "project frontend" run
* Sees node 2 (option):
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2552 -Dhttp.port=8082 "project frontend" run
* normal node:
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dhttp.port=8083 "project frontend" run

Test url from browser:
http://localhost:[port]/ping
http://localhost:[port]/hello/foobar
http://localhost:[port]/stop (shutdown actor system)

Run back-end cluster app:
* Seed node 1(optional):
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2551 "project backend" run
* Sees node 2 (optional):
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2552 "project backend" run
* normal node:
sbt -Djava.library.path=./sigar -Dconfig=cluster "project backend" run

