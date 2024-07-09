FROM gradle:7.5.1-jdk17 as build

WORKDIR /home/gradle

COPY --chown=gradle:gradle . .

RUN gradle clean build


FROM openjdk:17-jdk-alpine as runtime

COPY --from=build /home/gradle/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

RUN mkdir -p /app && (cd /app; jar -xf /app.jar)

ENTRYPOINT ["java","-cp","/app:/app/BOOT-INF/classes:/app/BOOT-INF/lib/*","net.serg.resourceapigateway.ResourceApiGatewayApplication"]
