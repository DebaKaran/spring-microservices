# -------- STAGE 1: BUILD --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# 1. Copy Maven wrapper + pom first
COPY mvnw pom.xml ./
COPY .mvn .mvn

# 2. Download dependencies (cached unless pom.xml changes)
RUN ./mvnw -B dependency:go-offline

# 3. Now copy source code
COPY src src

# 4. Build
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B package -DskipTests

# Extract layers
RUN java -Djarmode=layertools -jar target/*.jar extract
COPY .mvn .mvn

# 2. Download dependencies (cached unless pom.xml changes)
RUN ./mvnw -B dependency:go-offline

# 3. Now copy source code
COPY src src

# 4. Build
RUN ./mvnw -B package -DskipTests

# Extract layers
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
