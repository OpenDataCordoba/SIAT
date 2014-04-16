#! /bin/sh

cd build
java -cp .:../lib/js.jar coop.tecso.grs.GrsRun $1
