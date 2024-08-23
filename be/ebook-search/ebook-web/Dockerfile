FROM eclipse-temurin:21-jre-alpine
COPY build/libs/*.jar app.jar
VOLUME /app/config
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.additional-location=file:/app/config/"]