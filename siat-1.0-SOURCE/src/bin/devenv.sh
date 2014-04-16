#!/bin/sh

#ejemplo variables de entorno para siatgpl

export LANG=es_AR.iso88591

export ANT_HOME=~/opt/apache-ant-1.8.1
export JAVA_HOME=~/opt/jdk1.6.0_20
export CATALINA_BASE=~/opt/apache-tomcat-7.0.14-8070
export CATALINA_HOME=$CATALINA_BASE

export PATH=$JAVA_HOME/bin:$PATH
export PATH=$CATALINA_HOME/bin:$PATH
export PATH=$ANT_HOME/bin:$PATH

export CATALINA_OPTS="-Xmx1200m  -XX:MaxPermSize=256m"
export ANT_OPTS="-Xmx1200m"

#para debug remoto
#export CATALINA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "$CATALINA_OPTS 

export SWE_CHECKLOGIN=off

