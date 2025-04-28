FROM gradle:8.6-jdk21 AS builder
WORKDIR /app
COPY checkstyle/checkstyle.xml /app/checkstyle/
COPY build.gradle settings.gradle gradle.properties /app/
COPY gradle /app/gradle
RUN gradle dependencies --no-daemon

COPY src /app/src
RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/application.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "application.jar"]