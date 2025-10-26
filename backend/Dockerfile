FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy ONLY pom.xml trước (để cache dependencies)
COPY pom.xml .

# Download dependencies TRƯỚC (layer này được cache)
RUN mvn dependency:go-offline -B

# Sau đó mới copy source code
COPY src ./src

# Build (nếu chỉ code thay đổi, không download lại dependencies)
RUN mvn clean package -DskipTests -B assembly:single

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ShopManagement-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]