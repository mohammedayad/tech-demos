FROM fabric8/java-centos-openjdk11-jdk:1.6.3

ENV JAVA_APP_JAR microservice-demo-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTIONS -Xmx265m

EXPOSE 8080

RUN chmod -R 777 /deployments/

ADD target/$JAVA_APP_JAR /deployments/

#docker build -t app/myspring:v2 . ----create the image form the dockerfile
#docker run -it -p 8080:8080 app/myspring:v2
#kubectl create deployment microservice-demo --image=app/myspring:v2 --dry-run -o=yaml > deployment.yaml
# echo --- >> deployment.yaml
# kubectl create service clusterip microservice-demo --tcp=8080:8080 --dry-run -o=yaml >> deployment.yaml
# kubectl apply -f deployment.yaml

#we can use fabric8 plugin instead of using dockerfile to create the image
#mvn io.fabric8:fabric8-maven-plugin:3.3.5:setup