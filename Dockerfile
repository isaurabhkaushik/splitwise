################################################################
#  BUILD
################################################################
FROM maven:3.6.3-openjdk-11 AS builder
COPY splitwise-backend/pom.xml /tmp/pom.xml
COPY splitwise-backend/src /tmp/src
WORKDIR /tmp/
RUN mvn clean && mvn install

################################################################
#  MAIN
################################################################
FROM alpine:3.10
RUN echo "https://dl-4.alpinelinux.org/alpine/v3.10/main" >/etc/apk/repositories
RUN echo "https://dl-4.alpinelinux.org/alpine/v3.10/community" >>/etc/apk/repositories
RUN apk add --no-cache --update curl ca-certificates && update-ca-certificates && apk add --no-cache tzdata
RUN apk --no-cache add openjdk11
COPY --from=builder /tmp/target/manga-lo-backend.jar /manga-lo-backend.jar
CMD java -Dspring.profiles.active=${PROFILE} -jar manga-lo-backend.jar
EXPOSE 8004