FROM java:openjdk-8u111-jre-alpine
VOLUME /tmp
COPY build/libs/auth-warmup-0.1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]