package dev.michaeldubose.productcompositeservice;

import dev.michaeldubose.productcompositeservice.services.ProductCompositeServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackageClasses = {ProductCompositeServiceImpl.class, dev.michaeldubose.api.NoOp.class, dev.michaeldubose.util.NoOp.class})
@ConfigurationPropertiesScan("dev.michaeldubose.productcompositeservice.config")
public class ProductCompositeServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductCompositeServiceApplication.class, args);
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
