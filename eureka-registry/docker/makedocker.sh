#!/bin/sh
jarfile=`find ../build/libs/ -regex ".*eureka-registry.*[0-9]\.[0-9]?\-SNAPSHOT.jar"`
if [ -z "$jarfile" ]
then
    jarfile=`find ../build/libs/ -regex ".*eureka-registry.*[0-9]\.[0-9]?.jar"`
fi
mv $jarfile ./eureka-registry.jar
docker build -t zjtech/eureka-registry:latest .
rm -f ./eureka-registry.jar
