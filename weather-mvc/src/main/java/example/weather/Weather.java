package example.weather;

import java.util.List;
import org.apache.fory.json.annotation.JsonProperty;

public record Weather(
    String city,
    @JsonProperty("temperature_c") int temperature,
    boolean raining,
    List<String> tips) {}
