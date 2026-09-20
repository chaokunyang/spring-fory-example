package example.weather;

import org.apache.fory.json.ForyJson;
import org.apache.fory.json.annotation.JsonProperty.Include;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ForyJsonConfiguration {
  @Bean
  public ForyJson foryJson() {
    return ForyJson.builder()
        .defaultPropertyInclusion(Include.NON_EMPTY)
        .build();
  }
}
