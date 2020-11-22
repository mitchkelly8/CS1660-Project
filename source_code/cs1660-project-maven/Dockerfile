#FROM openjdk:8-jdk
#MAINTAINER Mitch Kelly
#RUN apt-get update
#RUN apt-get install -y maven 
#COPY pom.xml /usr/local/service/pom.xml
#COPY src /usr/local/service/src
#COPY Data /usr/local/service/Data
#WORKDIR /usr/local/service

FROM maven:3.6.0-jdk-11-slim
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN mvn package 
CMD ["mvn", "exec:java", "-D", "exec.mainClass=com.cs1660.Driver"]

#CMD ["java", "-cp", "target/cs1660-project-maven-0.0.1-SNAPSHOT.jar", "com.cs1660.Driver"]