FROM openjdk:8-jdk-alpine
RUN mkdir -p app
WORKDIR /app
COPY target/scala-2.12/gaia.jar /app/gaia.jar
COPY static /app/static
ENV AWS_ACCESS_KEY_ID sccess_key
ENV AWS_SECRET_ACCESS_KEY secret_key
EXPOSE 8080
ENTRYPOINT java -jar gaia.jar