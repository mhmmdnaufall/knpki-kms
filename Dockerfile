FROM openjdk:23-slim

ENV APP_PORT=8080

COPY ./build/libs/knpki-kms-0.0.1-SNAPSHOT.jar /app/knpki-kms.jar

EXPOSE $APP_PORT

CMD java -jar /app/knpki-kms.jar --server.port="$APP_PORT"