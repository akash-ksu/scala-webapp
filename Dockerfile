FROM java:8

RUN mkdir -p /opt/egen/event-source-core

ADD /target/scala-2.12/event-source-core-assembly-0.1.0-SNAPSHOT.jar /opt/egen/event-source-core/

RUN chmod +x /opt/egen/event-source-core/event-source-core-assembly-0.1.0-SNAPSHOT.jar

ADD event-source-core.sh /opt/egen/event-source-core
RUN chmod +x /opt/egen/event-source-core/event-source-core.sh

EXPOSE 8080

CMD ["/opt/egen/event-source-core/event-source-core.sh"]