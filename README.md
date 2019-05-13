# Vertx Playground

## Introduction

A sample project based on Java 11 using the following lightweight stack:
- [Vert.x](https://vertx.io/docs)
  - [vertx-core](https://vertx.io/docs/vertx-core/java/)
  - [vertx-web](https://vertx.io/docs/vertx-web/java/)
  - [vertx-config](https://vertx.io/docs/vertx-config/java/)
- [Google Guice](https://github.com/google/guice/wiki/GettingStarted)
- [Flyway](https://flywaydb.org/documentation)
- [jOOQ](https://www.jooq.org/doc/3.11/manual/)

For tests (unit and integration tests) the following additional libraries are used:
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Vert.x JUnit 5 integration](https://vertx.io/docs/vertx-junit5/java/)
- [AssertJ](https://assertj.github.io/doc/)
- [vertx-web-client](https://vertx.io/docs/vertx-web-client/java/)

## Usage

### Running the Application
The application is started by simply running the main method of `VertxPlaygroundApplication` from within the IDE or by executing the gradle task `run`.

### Running Unit Tests
Unit tests can be executed from within the IDE or using the gradle task `test`.

### Running Integration Tests
Integration tests can be executed against a database in a docker container in order to not override the local database:
- Build the docker image: `docker build -t playground-inttest-db .`
- Start the docker container: `docker run -p 5433:5432 playground-inttest-db`
- Run integration test(s) from within the IDE
- If you want to debug: set a breakpoint in the test-method and execute: `psql -U playground -p 5433`. 
- Stop the running database container: `docker container stop playground-inttest-db`

IMPORTANT HINT: When using Docker Toolbox mapped ports target to `192.168.99.100` instead of `localhost`. Therefore, the value of the `url` attribute in the file `v` has to be modified accordingly.   

