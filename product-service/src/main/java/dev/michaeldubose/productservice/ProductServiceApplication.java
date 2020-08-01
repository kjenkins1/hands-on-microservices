package dev.michaeldubose.productservice;

import dev.michaeldubose.productservice.services.ProductServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackageClasses = {ProductServiceImpl.class, dev.michaeldubose.api.NoOp.class, dev.michaeldubose.util.NoOp.class})
public class ProductServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }

}
