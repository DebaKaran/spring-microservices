# -------- STAGE 1: BUILD --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy everything needed to build
COPY . .

# Build Spring Boot layered jar
RUN ./mvnw clean package -DskipTests

# Extract layers from the jar
RUN java -Djarmode=layertools -jar target/*.jar extract


# -------- STAGE 2: RUNTIME --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy layers separately (Docker cache friendly)
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
