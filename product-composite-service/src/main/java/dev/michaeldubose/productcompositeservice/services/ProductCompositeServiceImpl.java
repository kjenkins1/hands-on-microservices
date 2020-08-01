package dev.michaeldubose.productcompositeservice.services;

import dev.michaeldubose.api.composite.product.*;
import dev.michaeldubose.api.core.product.Product;
import dev.michaeldubose.api.core.recommendation.Recommendation;
import dev.michaeldubose.api.core.review.Review;
import dev.michaeldubose.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

  private final ServiceUtil serviceUtil;
  private final ProductCompositeIntegration integration;

  @Autowired
  public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public ProductAggregate getProduct(int productId) {
    Product product = integration.getProduct(productId);
    List<Recommendation> recommendations = integration.getRecommendations(productId);
    List<Review> reviews = integration.getReviews(productId);
    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) {
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    List<RecommendationSummary> recommendationSummaries = recommendations.stream()
      .map(r -> new RecommendationSummary(
        r.getRecommendationId(),
        r.getAuthor(),
        r.getRate()))
      .collect(Collectors.toList());

    List<ReviewSummary> reviewSummaries = reviews.stream()
      .map(r -> new ReviewSummary(
        r.getReviewId(),
        r.getAuthor(),
        r.getSubject()))
      .collect(Collectors.toList());

    String productAddress = product.getServiceAddress();
    String reviewAddress = reviews.stream().findFirst().map(Review::getServiceAddress).orElse("");
    String recommendationAddress = recommendations.stream().findFirst().map(Recommendation::getServiceAddress).orElse("");
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
}
