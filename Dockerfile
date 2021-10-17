FROM alpine
RUN apk add openjdk13

ENV JAVA_HOME /usr/lib/jvm/java-13-openjdk
ENV PATH $PATH:$JAVA_HOME/bin

FROM openjdk:13-alpine
COPY ./target/jwt_auth_demo-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
