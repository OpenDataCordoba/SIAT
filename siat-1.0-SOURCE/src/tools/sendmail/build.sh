#!/bin/bash
APPNAME="Sendmail"
VERSION="1.0"
CLASSPATH=$(for f in lib/*; do echo -n  $f:; done;)

echo "Compilando $APPNAME v$VERSION"

mkdir -p build;

javac -cp $CLASSPATH -d build src/*;

cp lib/* build;

cd build;

for file in $(ls *.jar); do jar xf $file;rm META\-INF/MANIFEST.MF;done;

rm *.jar;

cd ..;

echo "Creando $APPNAME.jar"
jar cmf manifiest sendmail.jar -C build . -C lib .;

