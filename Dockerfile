# Stage 1: Build
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && \
    ./mvnw clean package -DskipTests

# Stage 2: Runtime  
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copiar JAR do stage anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]