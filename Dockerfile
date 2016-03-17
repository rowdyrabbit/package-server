FROM java:8

ADD ./bin/do-assignment-server-jar.jar  /opt/do/do-assignment-server-jar.jar

CMD ["java","-jar","/opt/do/do-assignment-server-jar.jar"]

EXPOSE 8080
