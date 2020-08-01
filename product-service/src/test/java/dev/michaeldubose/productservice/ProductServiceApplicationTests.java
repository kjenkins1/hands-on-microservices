package dev.michaeldubose.productservice;

import dev.michaeldubose.api.core.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

  @Autowired
  private ProductService productService;

  @Autowired
  private WebTestClient client;

  @Test
  void contextLoads() {
    assertThat(productService).isNotNull();
  }

  @Test
  public void getProductById() {
    int productId = 1;

    client.get()
      .uri("/product/" + productId)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.productId").isEqualTo(productId);
  }

  @Test
  public void getProductInvalidParameterString() {

    client.get()
      .uri("/product/no-integer")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.path").isEqualTo("/product/no-integer")
      .jsonPath("$.message").isEqualTo("Type mismatch.");
  }

  @Test
  public void getProductNotFound() {

    int productIdNotFound = 13;

    client.get()
      .uri("/product/" + productIdNotFound)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
      .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
  }

  @Test
  public void getProductInvalidParameterNegativeValue() {

    int productIdInvalid = -1;

    client.get()
      .uri("/product/" + productIdInvalid)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
      .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
  }
}
