FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/syo-0.0.1.jar
COPY ${JAR_FILE} app_syo.jar
EXPOSE 8090
ENTRYPOINT [ "java", "-jar", "app_syo.jar" ]