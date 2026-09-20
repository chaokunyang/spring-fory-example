package example.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.apache.fory.json.ForyJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
    classes = WeatherMvcTest.Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class WeatherMvcTest {
  @Autowired WebApplicationContext context;
  @Autowired ForyJson json;

  @SpringBootConfiguration
  @EnableAutoConfiguration
  @Import({WeatherController.class, ForyJsonConfiguration.class})
  static class Application {}

  @Test
  void currentWeather() throws Exception {
    String response = MockMvcBuilders.webAppContextSetup(context).build()
        .perform(get("/weather"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    assertThat(json.fromJson(response, Weather.class))
        .isEqualTo(new Weather("Toronto", 25, false, null));
  }

  @Test
  void roundTrip() throws Exception {
    String response = MockMvcBuilders.webAppContextSetup(context).build()
        .perform(post("/weather")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"city":"Vancouver","temperature_c":18,"raining":true,"tips":["Take an umbrella"]}
                """))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    assertThat(response)
        .contains("\"city\":\"Vancouver\"", "\"temperature_c\":18", "\"raining\":true")
        .doesNotContain("\"temperature\":");
    assertThat(json.fromJson(response, Weather.class))
        .isEqualTo(new Weather("Vancouver", 18, true, List.of("Take an umbrella")));
  }
}
