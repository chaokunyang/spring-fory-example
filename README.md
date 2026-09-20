# Spring Fory JSON examples

Two Spring Boot 4 applications that use [Fory JSON](https://fory.apache.org/docs/json)
through [Spring Fory](https://github.com/chaokunyang/spring-fory): one with Spring MVC
and one with WebFlux.

The examples use weather data for Toronto and Vancouver, with temperatures in
degrees Celsius. All values are sample data; no weather API or credentials are
needed.

## Get the project

Install JDK 17 or later, then run:

```bash
git clone https://github.com/chaokunyang/spring-fory-example.git
cd spring-fory-example
```

On Windows, replace `./mvnw` with `mvnw.cmd`. The Maven Wrapper downloads Maven
on its first run, and Maven resolves the dependencies from Maven Central.

## Run the MVC application

```bash
./mvnw -pl weather-mvc spring-boot:run
```

The application starts on port 8080. Open
[http://localhost:8080/weather](http://localhost:8080/weather), or run this in
another terminal:

```bash
curl http://localhost:8080/weather
```

The response contains these values; JSON property order may differ:

```json
{ "city": "Toronto", "temperature_c": 25, "raining": false }
```

POST a weather object to see JSON deserialization and serialization together:

```bash
curl http://localhost:8080/weather \
  -H 'Content-Type: application/json' \
  --data '{"city":"Vancouver","temperature_c":18,"raining":true,"tips":["Take an umbrella"]}'
```

The endpoint returns the same weather values. Stop the application with Ctrl+C.

## Run the WebFlux application

From the repository root, run:

```bash
./mvnw -pl weather-webflux spring-boot:run
```

This application uses port 8081, so it can run alongside the MVC application.
It has the same GET and POST `/weather` endpoints and a streaming endpoint:

```bash
curl http://localhost:8081/weather
curl -N http://localhost:8081/weather/updates
```

The stream sends two sample updates, one second apart, then completes:

```text
{"city":"Toronto","temperature_c":25,"raining":false}
{"city":"Toronto","temperature_c":18,"raining":true,"tips":["Take an umbrella"]}
```

Each line is a complete JSON value (NDJSON). Stop the application with Ctrl+C.

## Find the application code

| Application | Entry point                                                                                 | Source                                                                         |
| ----------- | ------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------ |
| MVC         | [WeatherApplication](weather-mvc/src/main/java/example/weather/WeatherApplication.java)     | [weather-mvc/src/main/java](weather-mvc/src/main/java/example/weather)         |
| WebFlux     | [WeatherApplication](weather-webflux/src/main/java/example/weather/WeatherApplication.java) | [weather-webflux/src/main/java](weather-webflux/src/main/java/example/weather) |

Each module is a self-contained application with its own web starter, model,
controller, and Fory configuration. Both use the customized article example:
`temperature` is written as `temperature_c`, and empty `tips` are omitted.

The Boot starter registers Fory automatically. The manual Spring 7 configurations
from the articles remain as reference code in each module's
`src/test/java/example/manual` package; they are not loaded by the Boot applications.

## Build and test

```bash
./mvnw clean verify
```

This runs the tests and builds two executable jars. You can also start them without
Maven:

```bash
java -jar weather-mvc/target/weather-mvc-1.0-SNAPSHOT.jar
java -jar weather-webflux/target/weather-webflux-1.0-SNAPSHOT.jar
```

Run each command in a separate terminal. To run tests without packaging, use
`./mvnw test`.

The tests cover MVC requests, reactive requests and NDJSON output, JSON round
trips, and empty-property handling. The HTTP tests load the real application
configuration and use MockMvc or WebTestClient. Separate MVC test fixtures cover
the initial article example before field renaming and custom configuration.

## Dependencies

The [POM](pom.xml) uses Spring Boot 4.0.7 and Spring Fory 1.1.0. The Fory starter
brings in the Fory JSON dependency; no separate Apache Fory version is specified.

The JSON property annotation is Fory's
`org.apache.fory.json.annotation.JsonProperty`. Jackson annotations and
`spring.jackson.*` settings do not configure these examples.
