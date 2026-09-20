package example.manual;

import io.github.chaokunyang.springfory.ForyJsonHttpMessageConverter;
import org.apache.fory.json.ForyJson;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class ForyMvcConfiguration implements WebMvcConfigurer {
  private final ForyJson json;

  public ForyMvcConfiguration(ForyJson json) {
    this.json = json;
  }

  @Override
  public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
    builder.withJsonConverter(new ForyJsonHttpMessageConverter(json));
  }
}
