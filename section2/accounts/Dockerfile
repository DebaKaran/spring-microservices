# -------- STAGE 1: BUILD --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .
RUN ./mvnw clean package -DskipTests


# -------- STAGE 2: RUNTIME --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar accounts.jar
ENTRYPOINT ["java", "-jar", "accounts.jar"]