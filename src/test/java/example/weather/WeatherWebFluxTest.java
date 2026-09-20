package example.weather;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.apache.fory.json.ForyJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    classes = WeatherWebFluxTest.Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = "spring.main.web-application-type=reactive")
class WeatherWebFluxTest {
  @Autowired ApplicationContext context;
  @Autowired ForyJson json;

  @SpringBootConfiguration
  @EnableAutoConfiguration
  @Import({ReactiveWeatherController.class, ForyJsonConfiguration.class})
  static class Application {}

  private WebTestClient client() {
    return WebTestClient.bindToApplicationContext(context).configureClient()
        .responseTimeout(Duration.ofSeconds(10)).build();
  }

  @Test
  void currentWeather() {
    String response = client().get().uri("/weather")
        .exchange().expectStatus().isOk()
        .expectBody(String.class).returnResult().getResponseBody();
    assertThat(json.fromJson(response, Weather.class))
        .isEqualTo(new Weather("Toronto", 25, false, null));
  }

  @Test
  void monoRoundTrip() {
    String response = client().post().uri("/weather")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("""
            {"city":"Toronto","temperature_c":0,"raining":false,"tips":[]}
            """)
        .exchange().expectStatus().isOk()
        .expectBody(String.class).returnResult().getResponseBody();
    assertThat(response)
        .contains("\"city\":\"Toronto\"", "\"temperature_c\":0", "\"raining\":false")
        .doesNotContain("tips");
  }

  @Test
  void ndjsonUpdates() {
    String response = client().get().uri("/weather/updates")
        .accept(MediaType.APPLICATION_NDJSON)
        .exchange().expectStatus().isOk()
        .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
        .expectBody(String.class).returnResult().getResponseBody();
    List<String> lines = response.lines().toList();
    assertThat(lines).hasSize(2);
    assertThat(lines.get(0)).contains("\"temperature_c\":25").doesNotContain("tips");
    assertThat(json.fromJson(lines.get(0), Weather.class))
        .isEqualTo(new Weather("Toronto", 25, false, null));
    assertThat(json.fromJson(lines.get(1), Weather.class))
        .isEqualTo(new Weather("Toronto", 18, true, List.of("Take an umbrella")));
  }
}
