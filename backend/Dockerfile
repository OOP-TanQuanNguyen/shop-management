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

# Copy file .jar từ stage build
COPY --from=builder /app/target/*.jar app.jar

# Copy file .env nếu bạn cần dùng dotenv trong container
COPY .env .

# Mở port cho Socket server (nếu có)
EXPOSE 9999

# Lệnh chạy chương trình Java
ENTRYPOINT ["java", "-jar", "app.jar"]
