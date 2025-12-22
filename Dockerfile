FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copy pom.xml and download dependencies 
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Use JRE for runtime (smaller image)
FROM eclipse-temurin:17-jre-alpine
# Set working directory
WORKDIR /app
# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]