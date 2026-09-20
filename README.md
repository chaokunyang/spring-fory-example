# Spring Fory JSON examples

Runnable examples and tests for using [Fory JSON](https://fory.apache.org/docs/json)
with Spring Boot 4 and Spring Framework 7 through
[Spring Fory](https://github.com/chaokunyang/spring-fory).

The examples use weather data for Toronto and Vancouver, with temperatures in
degrees Celsius. All values are sample data; no weather API or credentials are
needed.

## Run the tests

Install JDK 17 or later, then run:

```bash
git clone https://github.com/chaokunyang/spring-fory-example.git
cd spring-fory-example
./mvnw test
```

On Windows, use `mvnw.cmd test`. The Maven Wrapper downloads Maven on its first
run, and Maven resolves the dependencies from Maven Central.

The suite runs 9 tests. To run just the reactive examples:

```bash
./mvnw -Dtest=WeatherWebFluxTest test
```

## Find an example

All example classes and tests are in
[`src/test/java/example/weather`](src/test/java/example/weather).

| Test                                                                        | What it demonstrates                                                                                |
| --------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| [DefaultWeatherTest](src/test/java/example/weather/DefaultWeatherTest.java) | MVC GET and POST with the starter's default Fory instance and the initial `temperature` field name. |
| [WeatherMvcTest](src/test/java/example/weather/WeatherMvcTest.java)         | MVC GET and POST with `temperature_c` and a custom Fory bean that omits empty properties.           |
| [WeatherWebFluxTest](src/test/java/example/weather/WeatherWebFluxTest.java) | Reactive GET and POST, plus two weather updates encoded as NDJSON.                                  |
| [ForyJsonTest](src/test/java/example/weather/ForyJsonTest.java)             | Direct JSON round trips, omission of empty lists, and preservation of zero and false values.        |

The [`initial`](src/test/java/example/weather/initial) package contains the first
version of the weather model and controller. The main `example.weather` package
contains the customized version used later in the articles.

[`ForyMvcConfiguration`](src/test/java/example/weather/ForyMvcConfiguration.java)
and
[`ForyWebFluxConfiguration`](src/test/java/example/weather/ForyWebFluxConfiguration.java)
show manual registration for applications using Spring Framework 7 without Boot.
They are compiled with the examples; the HTTP tests exercise the Boot starter's
automatic configuration.

Each HTTP test loads its own controller and configuration. MVC tests use MockMvc,
and WebFlux tests bind WebTestClient to the application context. Running the suite
does not start a server on port 8080. The example controllers live under
`src/test/java` so both MVC and WebFlux versions can be tested in one project.

## Dependencies

The [POM](pom.xml) uses Spring Boot 4.0.7 and Spring Fory 1.1.0. The Fory starter
brings in the Fory JSON dependency; no separate Apache Fory version is specified.

The JSON property annotation is Fory's
`org.apache.fory.json.annotation.JsonProperty`. Jackson annotations and
`spring.jackson.*` settings do not configure these examples.
