package example.weather;

import java.time.Duration;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveWeatherController {
  @GetMapping("/weather")
  public Mono<Weather> current() {
    return Mono.just(new Weather("Toronto", 25, false, List.of()));
  }

  @PostMapping("/weather")
  public Mono<Weather> echo(@RequestBody Mono<Weather> weather) {
    return weather;
  }

  @GetMapping(
      path = "/weather/updates",
      produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Weather> updates() {
    return Flux.just(
            new Weather("Toronto", 25, false, List.of()),
            new Weather("Toronto", 18, true, List.of("Take an umbrella")))
        .delayElements(Duration.ofSeconds(1));
  }
}
