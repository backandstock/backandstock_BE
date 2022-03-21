#FROM openjdk:8-jdk-alpine
#ARG JAR_FILE=build/libs/miniBacktesting_be-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} minibacktesting.jar
#ENTRYPOINT ["java","-jar","/minibacktesting.jar"]
#
FROM java:9
ARG JAR_FILE=build/libs/miniBacktesting_be-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} minibacktesting.jar
ENTRYPOINT ["java","-jar", "/minibacktesting.jar"]