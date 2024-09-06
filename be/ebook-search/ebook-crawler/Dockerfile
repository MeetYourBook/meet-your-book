FROM amazoncorretto:21-alpine
COPY build/libs/*.jar app.jar
WORKDIR /app
VOLUME /app/config
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.additional-location=file:/app/config/"]