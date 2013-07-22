akka-sbt-study
==============

setup project environment
--------------------------

* Install sbt: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html
* in project home folder, start `sbt` and run `eclipse with-source=true` command

* open eclipse, import all projects

Run front-end cluster app
--------------------------

* Seed node 1:
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2551 "project frontend" run
* Sees node 2 (option):
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2552 -Dhttp.port=8082 "project frontend" run
* normal node:
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dhttp.port=8083 "project frontend" run

Test url from browser
---------------------

* http://localhost:8081/ping
* http://localhost:8081/hello/foobar
* http://localhost:8081/stop (shutdown actor system)

Run back-end cluster app
-------------------------

* Seed node 1(optional):
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2551 "project backend" run
* Sees node 2 (optional):
sbt -Djava.library.path=./sigar -Dconfig=cluster -Dakka.port=2552 "project backend" run
* normal node:
sbt -Djava.library.path=./sigar -Dconfig=cluster "project backend" run

Start Nginx reserve proxy
--------------------------

`nginx -c ./nginx/nginx.conf`

`nginx -s stop` to stop proxy
