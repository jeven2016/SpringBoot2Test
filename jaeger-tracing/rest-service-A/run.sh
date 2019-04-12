#!/bin/bash
export JAEGER_SERVICE_NAME=rest-service-A
export JAEGER_ENDPOINT=http://10.113.49.230:5805/api/traces
export JAEGER_SAMPLER_TYPE=const
export JAEGER_SAMPLER_PARAM=1
java -jar build/libs/rest-serviceA-0.1-SNAPSHOT.jar