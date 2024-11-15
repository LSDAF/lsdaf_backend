# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-17-alpine AS compiler
COPY env env/
COPY build_parent build_parent/
COPY lsadf_bom/ lsadf_bom/
COPY lsadf_dependencies lsadf_dependencies/
COPY lsadf_api lsadf_api/
COPY pom.xml .
COPY ./docker/startup.sh .
# Delete application-local.yml to prevent ambiguous configurations
#RUN rm lsadf_api/src/main/resources/application-local.yml

RUN mvn clean install -DskipTests
RUN mv lsadf_api/target/*.jar lsadf_api/target/lsadf_backend.jar

FROM openjdk:17-alpine AS builder

COPY --from=compiler lsadf_api/target/lsadf_backend.jar lsadf_backend.jar
COPY --from=compiler lsadf_api/target/classes/logback-docker.xml logback-docker.xml

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
COPY --from=builder logback-docker.xml ./

RUN mkdir logs

RUN chmod +x startup.sh

ENTRYPOINT ["./startup.sh"]