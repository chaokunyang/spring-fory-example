package example.weather;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.fory.json.ForyJson;
import org.junit.jupiter.api.Test;

class ForyJsonTest {
  @Test
  void defaultRoundTrip() {
    ForyJson json = ForyJson.builder().build();
    Weather weather = new Weather("Toronto", 25, false, List.of());
    byte[] bytes = json.toJsonBytes(weather);
    assertThat(new String(bytes, StandardCharsets.UTF_8))
        .contains("\"city\":\"Toronto\"", "\"temperature_c\":25", "\"tips\":[]");
    assertThat(json.fromJson(bytes, Weather.class)).isEqualTo(weather);
  }

  @Test
  void emptyTips() {
    ForyJson json = new ForyJsonConfiguration().foryJson();
    Weather weather = new Weather("Toronto", 0, false, List.of());
    String encoded = json.toJson(weather);
    assertThat(encoded)
        .contains("\"city\":\"Toronto\"", "\"temperature_c\":0", "\"raining\":false")
        .doesNotContain("tips");
    Weather decoded = json.fromJson(encoded, Weather.class);
    assertThat(decoded.temperature()).isZero();
    assertThat(decoded.raining()).isFalse();
    assertThat(decoded.tips()).isNull();
  }
}
