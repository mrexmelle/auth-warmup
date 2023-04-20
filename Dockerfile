FROM bellsoft/liberica-openjdk-alpine:17
VOLUME /tmp
COPY build/libs/auth-warmup-0.1.0.jar app.jar
COPY keys keys
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080

LABEL org.opencontainers.image.source https://github.com/OWNER/REPO