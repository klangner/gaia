FROM openjdk:8-jdk-alpine
RUN mkdir -p app
WORKDIR /app
COPY target/scala-2.12/gaia.jar /app/gaia.jar
COPY static /app/static
COPY scenarios /app/scenarios
EXPOSE 8080
ENTRYPOINT java -jar gaia.jar