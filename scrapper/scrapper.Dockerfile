FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} scrapper.jar
ENTRYPOINT ["java","-jar","/scrapper.jar"]
