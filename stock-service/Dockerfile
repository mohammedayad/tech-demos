#FROM adoptopenjdk:11-jdk-hotspot
FROM fabric8/java-centos-openjdk11-jdk:1.6.3


ENV JAVA_APP_JAR stock-service-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTIONS -Xmx265m

EXPOSE 8081

RUN chmod -R 777 /deployments/

ADD target/$JAVA_APP_JAR /deployments/

# docker build -t mohammedayad/stock-service:v1 .

