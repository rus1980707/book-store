# Етап 1: Компіляція
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Збираємо додаток всередині Docker (пропускаємо тести для швидкості)
RUN mvn clean package -DskipTests  -Dcheckstyle.skip

# Builder stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR application
COPY --from=build /app/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Runtime stage
FROM eclipse-temurin:21-jdk-alpine
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
