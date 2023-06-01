#FROM adoptopenjdk:11-jdk-hotspot
FROM fabric8/java-centos-openjdk11-jdk:1.6.3


ENV JAVA_APP_JAR microservice-gateway-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTIONS -Xmx265m

EXPOSE 8080

RUN chmod -R 777 /deployments/

ADD target/$JAVA_APP_JAR /deployments/

# docker build -t app/gateway:v1 .
# kubectl create deployment microservice-gateway --image=app/gateway:v1 --dry-run -o=yaml > deployment.yaml
# echo --- >> deployment.yaml
# kubectl create service nodeport microservice-gateway --tcp=8080:8080 --node-port=30000 --dry-run=client -o yaml >> deployment.yaml
# kubectl apply -f deployment.yaml

