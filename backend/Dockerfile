FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app
COPY . .
RUN mvn -B dependency:go-offline
EXPOSE 2025
CMD ["mvn", "exec:java", "-Dexec.mainClass=edu.ptithcm.Server"]
