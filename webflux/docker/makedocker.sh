#!/bin/sh
jarfile=`find ../build/libs/ -regex ".*webflux.*[0-9]\.[0-9]?\-SNAPSHOT.jar"`
if [ -z "$jarfile" ]
then
    jarfile=`find ../build/libs/ -regex ".*webflux.*[0-9]\.[0-9]?.jar"`
fi
mv $jarfile ./webflux.jar
docker build -t zjtech/webflux:latest .
rm -f ./webflux.jar
