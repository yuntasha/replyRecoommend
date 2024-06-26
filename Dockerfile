FROM openjdk:17-jdk-slim
EXPOSE 8080
ARG JAR_FILE=/build/libs/replyRecoommend-0.0.1-SNAPSHOT.jar
VOLUME ["/var/log"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app.jar"]