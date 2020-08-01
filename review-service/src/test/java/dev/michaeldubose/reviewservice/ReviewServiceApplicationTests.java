package dev.michaeldubose.reviewservice;

import dev.michaeldubose.api.core.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewServiceApplicationTests {

  @Autowired
  private WebTestClient client;

  @Autowired
  private ReviewService reviewService;

  @Test
  void contextLoads() {
    assertThat(reviewService).isNotNull();
  }

  @Test
  public void getReviewsByProductId() {

    int productId = 1;

    client.get()
      .uri("/review?productId=" + productId)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(3)
      .jsonPath("$[0].productId").isEqualTo(productId);
  }

  @Test
  public void getReviewsMissingParameter() {

    client.get()
      .uri("/review")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(BAD_REQUEST)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.path").isEqualTo("/review")
      .jsonPath("$.message").isEqualTo("Required int parameter 'productId' is not present");
  }

  @Test
  public void getReviewsInvalidParameter() {

    client.get()
      .uri("/review?productId=no-integer")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(BAD_REQUEST)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.path").isEqualTo("/review")
      .jsonPath("$.message").isEqualTo("Type mismatch.");
  }

  @Test
  public void getReviewsNotFound() {

    int productIdNotFound = 213;

    client.get()
      .uri("/review?productId=" + productIdNotFound)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(0);
  }

  @Test
  public void getReviewsInvalidParameterNegativeValue() {

    int productIdInvalid = -1;

    client.get()
      .uri("/review?productId=" + productIdInvalid)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.path").isEqualTo("/review")
      .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
  }
  
}
