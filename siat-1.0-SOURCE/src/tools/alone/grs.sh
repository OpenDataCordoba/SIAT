export LC_ALL=POSIX
export LANG=POSIX

JARS_PATH=./build/lib
CP=""
for i in "$JARS_PATH"/*.jar; do
      CP="$CP":"$i"
done

#grs
CP=../grs/build:../lib/js..jar:$CP


export CLASSPATH="$CP"

$JAVA_HOME/bin/java -Xmx1024m -Dalone.confpath=$1 -Dgrs.path='/mnt/privado' alone.GrsRun $@

