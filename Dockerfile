FROM java:8

ADD ./bin/package-server-jar.jar  /opt/do/package-server-jar.jar

CMD ["java","-jar","/opt/do/do-assignment-server-jar.jar"]

EXPOSE 8080
