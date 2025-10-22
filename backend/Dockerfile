# ============================
# 1️⃣ Build stage - Maven
# ============================
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy cấu hình Maven trước (để cache dependency)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy toàn bộ mã nguồn
COPY src ./src

# Build project (bỏ test cho nhanh)
RUN mvn clean package -DskipTests

# ============================
# 2️⃣ Runtime stage - Java
# ============================
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy file JAR đầy đủ dependencies (fat JAR)
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Mở port cho Socket server (chỉnh theo code)
EXPOSE 2025

# Lệnh chạy chương trình Java
ENTRYPOINT ["java", "-jar", "app.jar"]
