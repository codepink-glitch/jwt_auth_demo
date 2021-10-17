FROM maven:3.8.3-openjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM alpine
RUN apk add openjdk-13

ENV JAVA_HOME /usr/lib/jvm/java-13-openjdk
ENV PATH $PATH:$JAVA_HOME/bin

FROM openjdk:13-alpine
COPY --from=build /usr/src/app/target/jwt_auth_demo-1.0.jar /usr/app/jwt_auth_demo-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/app/jwt_auth_demo-1.0.jar"]
