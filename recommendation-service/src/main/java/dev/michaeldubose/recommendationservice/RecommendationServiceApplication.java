package dev.michaeldubose.recommendationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {RecommendationServiceApplication.class,
  dev.michaeldubose.api.NoOp.class, dev.michaeldubose.util.NoOp.class})
public class RecommendationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(RecommendationServiceApplication.class, args);
  }

}
