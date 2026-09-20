package example.manual;

import io.github.chaokunyang.springfory.ForyJsonDecoder;
import io.github.chaokunyang.springfory.ForyJsonEncoder;
import org.apache.fory.json.ForyJson;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration(proxyBeanMethods = false)
public class ForyWebFluxConfiguration implements WebFluxConfigurer {
  private final ForyJson json;

  public ForyWebFluxConfiguration(ForyJson json) {
    this.json = json;
  }

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    configurer.defaultCodecs().jacksonJsonEncoder(new ForyJsonEncoder(json));
    configurer.defaultCodecs().jacksonJsonDecoder(new ForyJsonDecoder(json));
  }
}
