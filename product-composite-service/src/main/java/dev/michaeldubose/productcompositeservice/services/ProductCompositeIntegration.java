package dev.michaeldubose.productcompositeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.michaeldubose.api.core.product.Product;
import dev.michaeldubose.api.core.recommendation.Recommendation;
import dev.michaeldubose.api.core.review.Review;
import dev.michaeldubose.productcompositeservice.config.ProductServiceConfig;
import dev.michaeldubose.productcompositeservice.config.RecommendationServiceConfig;
import dev.michaeldubose.productcompositeservice.config.ReviewServiceConfig;
import dev.michaeldubose.util.exceptions.InvalidInputException;
import dev.michaeldubose.util.exceptions.NotFoundException;
import dev.michaeldubose.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Component
public class ProductCompositeIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  @Autowired
  public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper mapper, ProductServiceConfig productServiceConfig, RecommendationServiceConfig recommendationServiceConfig, ReviewServiceConfig reviewServiceConfig) {
    this.restTemplate = restTemplate;
    this.mapper = mapper;

    productServiceUrl = "http://" + productServiceConfig.getHost() + ":" + productServiceConfig.getPort() + "/product/";
    recommendationServiceUrl = "http://" + recommendationServiceConfig.getHost() + ":" + recommendationServiceConfig.getPort() + "/recommendation?productId=";
    reviewServiceUrl = "http://" + reviewServiceConfig.getHost() + ":" + reviewServiceConfig.getPort() + "/review?productId=";
  }

  /**
   * Retrieve product by id
   * @param productId Id of the product to be retrieved
   * @return Product with id productId
   * @throws NotFoundException When no product is found matching the input id
   * @throws InvalidInputException When the input value is unprocessable
   * @throws HttpClientErrorException When a generic error occurs communicating with the product service
   */
  public Product getProduct(int productId) {
    try {
      String url = productServiceUrl + productId;
      LOG.debug("Will call getProduct API on URL: {}", url);

      Product product = restTemplate.getForObject(url, Product.class);
      if (product == null) {
        LOG.debug("No product found with id: {}", productId);
        throw new NotFoundException();
      }
      LOG.debug("Found a product with id: {}", product.getProductId());

      return product;
    } catch (HttpClientErrorException ex) {
      switch (ex.getStatusCode()) {
        case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
        case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(ex));
        default -> {
          LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusText());
          LOG.warn("Error body: {}", ex.getResponseBodyAsString());
          throw ex;
        }
      }
    }
  }

  public List<Recommendation> getRecommendations(int productId) {
    try {
      String url = recommendationServiceUrl + productId;

      LOG.debug("Will call getRecommendations API on URL: {}", url);
      List<Recommendation> recommendations = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {}).getBody();

      if (recommendations == null) {
        LOG.debug("Failed to retrieve recommendations for product with id: {}", productId);
        return Collections.emptyList();
      }

      LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
      return recommendations;

    } catch (Exception ex) {
      LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
      return Collections.emptyList();
    }
  }

  public List<Review> getReviews(int productId) {

    try {
      String url = reviewServiceUrl + productId;

      LOG.debug("Will call getReviews API on URL: {}", url);
      List<Review> reviews = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {}).getBody();

      if (reviews == null) {
        LOG.debug("Failed to retrieve reviews for product with id: {}", productId);
        return Collections.emptyList();
      }

      LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
      return reviews;

    } catch (Exception ex) {
      LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
      return Collections.emptyList();
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioEx) {
      return ex.getMessage();
    }
  }
}
