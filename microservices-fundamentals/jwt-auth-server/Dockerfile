FROM maven:3.8-openjdk-17-slim as build

WORKDIR /app

COPY src src
COPY pom.xml .

RUN mvn -B clean package -DskipTests

FROM openjdk:17-jdk as runtime

RUN mkdir -p /src/main/resources
COPY --from=build /app/target/jwt-auth-server-*.jar /app/auth-server.jar
COPY --from=build /app/src/main/resources/ /src/main/resources/

EXPOSE 8086

CMD ["/bin/sh", "-c", "java -jar /app/auth-server.jar"]