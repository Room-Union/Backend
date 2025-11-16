# Base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy only built jar file from host (not source code)
# (이 jar는 GitHub Actions에서 ./gradlew bootJar -x test 후 생성된 파일)
COPY build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application with AWS profile
ENTRYPOINT ["java", "-jar", "app.jar"]