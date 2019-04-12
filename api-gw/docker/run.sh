#!/bin/sh
gcdir="/var/log/zjtech/gc"
if [ ! -d "$gcdir" ]; then
 mkdir -p $gcdir
fi

heapConfig="-Xms1g -Xmx1g -XX:+UseG1GC -XX:MaxGCPauseMillis=100"
metaspaceConfig="-XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=2048M"
logGcConfig="-Xloggc:$gcdir/gc-$(date +%Y%m%d-%H%M%S).log -XX:+PrintGC -XX:-PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=2 -XX:GCLogFileSize=16M"

JAVA_OPTS="$JAVA_OPTS $heapConfig -XX:+HeapDumpOnOutOfMemoryError $metaspaceConfig $logGcConfig"
export JAVA_OPTS

# For production
java $JAVA_OPTS -jar /opt/tb/api-gw.jar

# For remote debugging (disabled by default)
#java $JAVA_OPTS -jar -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000" /var/slc/slc-backend.jar --spring.profiles.active=production