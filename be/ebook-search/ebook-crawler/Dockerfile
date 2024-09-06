FROM amazoncorretto:21-alpine

# 한국 시간으로 설정
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

COPY build/libs/*.jar app.jar
WORKDIR /app
VOLUME /app/config
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.additional-location=file:/app/config/"]