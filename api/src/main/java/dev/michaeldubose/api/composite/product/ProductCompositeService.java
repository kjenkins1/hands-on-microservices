package dev.michaeldubose.api.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Product Composite Service", description = "REST Api for composite product information")
@RequestMapping("/product-composite")
public interface ProductCompositeService {

  @Operation(
    summary = "${api.product-composite.get-composite-product.description}",
    description = "${api.product-composite.get-composite-product.notes}")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
    @ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
    @ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
  })
  @GetMapping(
    value = "/{productId}",
    produces = "application/json")
  ProductAggregate getProduct(@PathVariable int productId);
}
