FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
COPY gradle.properties /home/gradle/.gradle/
ENV SPRING_PROFILES_ACTIVE=test
RUN gradle build --no-daemon -x test --stacktrace

FROM openjdk:21-jdk-slim
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]