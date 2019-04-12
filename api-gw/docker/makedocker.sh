#!/bin/sh
jarfile=`find ../build/libs/ -regex ".*api-gw.*[0-9]\.[0-9]?\-SNAPSHOT.jar"`
if [ -z "$jarfile" ]
then
    jarfile=`find ../build/libs/ -regex ".*api-gw.*[0-9]\.[0-9]?.jar"`
fi
mv $jarfile ./api-gw.jar
docker build -t zjtech/api-gw:latest .
rm -f ./api-gw.jar
