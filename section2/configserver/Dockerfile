# -------- STAGE 1: BUILD --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy source
COPY . .

# Build Spring Boot layered jar
RUN ./mvnw clean package -DskipTests

# Extract layers
RUN java -Djarmode=layertools -jar target/*.jar extract


# -------- STAGE 2: RUNTIME --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Config Server talks to Git → needs git + certs
RUN apk add --no-cache git ca-certificates

# Copy layers separately (cache-friendly)
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

# Config Server default port
EXPOSE 8071

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
