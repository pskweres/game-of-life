#!/bin/bash

rm -rf build
mkdir build
javac -Xlint:unchecked -cp $GIRAPH_HOME/giraph-core/target/giraph-1.2.0-hadoop2-for-hadoop-2.5.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath) -d build/ src/pdd/*.java

cp giraph.jar ./gol.jar

jar -uvf gol.jar -C build/ .