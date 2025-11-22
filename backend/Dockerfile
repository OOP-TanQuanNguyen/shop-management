FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B assembly:single

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar

EXPOSE 2025
ENTRYPOINT ["java", "-jar", "app.jar"]
