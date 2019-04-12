FROM fiadliel/java8-jre
MAINTAINER Zjtech



# install jdk rpm
#RUN export http_proxy=http://cn-proxy.jp.oracle.com:80 && \
#    export https_proxy=http://cn-proxy.jp.oracle.com:80

RUN mkdir -p /opt/tb

ADD run.sh /opt/tb/run.sh
ADD webflux.jar /opt/tb/webflux.jar
RUN chmod a+x /opt/tb/run.sh

EXPOSE 9000-10000
ENTRYPOINT ["/opt/tb/run.sh"]