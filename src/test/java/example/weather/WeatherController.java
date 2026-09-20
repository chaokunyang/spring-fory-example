package example.weather;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
  @GetMapping("/weather")
  public Weather current() {
    return new Weather("Toronto", 25, false, List.of());
  }

  @PostMapping("/weather")
  public Weather echo(@RequestBody Weather weather) {
    return weather;
  }
}
