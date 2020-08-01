package dev.michaeldubose.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {ReviewServiceApplication.class, dev.michaeldubose.api.NoOp.class, dev.michaeldubose.util.NoOp.class})
public class ReviewServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReviewServiceApplication.class, args);
  }

}
