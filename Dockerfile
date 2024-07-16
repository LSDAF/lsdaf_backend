# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-17-alpine as compiler
WORKDIR /compiler
COPY env/ env/
COPY src/ src/
COPY pom.xml .
COPY startup.sh .

RUN mvn clean install -DskipTests
RUN ls -lrt
RUN ls -lrt target

FROM openjdk:17-alpine as builder

COPY --from=compiler target/ target/

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} lsadf_backend.jar
RUN java -Djarmode=layertools -jar lsadf_backend.jar extract
COPY --from=compiler /compiler/startup.sh .

# Stage 3: Create the final image
FROM openjdk:17-alpine

EXPOSE 8080

# Copy layers from the build stage
COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./
COPY --from=builder ./startup.sh ./

RUN chmod +x startup.sh

ENTRYPOINT ["./startup.sh"]