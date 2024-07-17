FROM gradle:7.5.1-jdk17 as build

WORKDIR /home/gradle

COPY --chown=gradle:gradle . .

RUN gradle clean build


FROM openjdk:17-jdk-alpine as runtime

# Did to wait for keycloak server to start
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY --from=build /home/gradle/build/libs/*.jar app.jar

RUN mkdir -p /app && (cd /app; jar -xf /app.jar)

ENTRYPOINT ["sh", "-c", "dockerize -wait http://keycloak-server:8086/auth/admin/master/console/#/resource-microservices -timeout 300s && java -cp '/app:/app/BOOT-INF/classes:/app/BOOT-INF/lib/*' net.serg.resourceapigateway.ResourceApiGatewayApplication"]