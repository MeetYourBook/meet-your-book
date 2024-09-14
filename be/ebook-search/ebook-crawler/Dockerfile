FROM amazoncorretto:21-alpine

# 한국 시간으로 설정
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

WORKDIR /app

COPY build/libs/*.jar app.jar

# Java 옵션을 환경 변수로 설정
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Duser.timezone=Asia/Seoul"

VOLUME /app/config

EXPOSE 8080

# JAVA_OPTS 환경 변수 사용
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.config.additional-location=file:/app/config/"]