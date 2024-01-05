FROM gradle:7.4.0-jdk17 as build

WORKDIR /home/gradle

COPY --chown=gradle:gradle . .

RUN gradle clean build


FROM openjdk:17-jdk-alpine as runtime

COPY --from=build /home/gradle/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

