FROM openjdk:11.0.5-jdk as builder

WORKDIR /code
ADD . ./
RUN ["./gradlew", "-q", "--no-daemon", "build"]

FROM openjdk:11.0.5-jre
EXPOSE 8080

ARG LIQUIBASE_TAG=1.0
ENV TAG ${LIQUIBASE_TAG}

COPY entrypoint.sh entrypoint.sh
COPY --from=builder /code/build/libs/liquibase-scripts-1.0.jar /app/liquibase-scripts.jar
ENTRYPOINT ["./entrypoint.sh"]
CMD ["default"]
