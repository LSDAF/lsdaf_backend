# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-17-alpine as compiler
COPY env env/
COPY src src/
COPY pom.xml .
COPY ./docker/startup.sh .

RUN mvn clean install -DskipTests
RUN ls -lrt
RUN mv target/*.jar target/lsadf_backend.jar

FROM openjdk:17-alpine as builder

COPY --from=compiler target/lsadf_backend.jar lsadf_backend.jar
COPY --from=compiler ../target/classes/logback-docker.xml logback-docker.xml

RUN ls -lrt

RUN java -Djarmode=layertools -jar lsadf_backend.jar extract
COPY --from=compiler startup.sh .

# Stage 3: Create the final image
FROM openjdk:17-alpine

EXPOSE 8080

#ENV SLEEP_ENABLED=true

# Copy layers from the build stage
COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./
COPY --from=builder startup.sh ./
COPY --from=builder ./logback-docker.xml ./

RUN mkdir logs

RUN chmod +x startup.sh

ENTRYPOINT ["./startup.sh"]