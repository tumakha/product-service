package product.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import product.dto.PriceLabelType;
import product.dto.ProductDTO;
import product.dto.ProductList;
import product.service.ProductService;

import javax.validation.constraints.Min;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Yuriy Tumakha
 */
@RestController
@Validated
@RequestMapping(path = "/v1/products", produces = APPLICATION_JSON_UTF8_VALUE)
public class ProductsResource {

  private static final Logger LOG = LoggerFactory.getLogger(ProductsResource.class);

  @Autowired
  private ProductService productService;

  @GetMapping
  @ApiOperation("Get products")
  public ResponseEntity<ProductList> getProducts(

      @Min(0)
      @ApiParam("Zero-based page index")
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,

      @Range(min = 1, max = 500)
      @ApiParam("Size of page to be returned")
      @RequestParam(name = "size", required = false, defaultValue = "100") Integer size,

      @RequestParam(name = "labelType", required = false, defaultValue = "ShowWasNow") PriceLabelType labelType) {

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(DESC, "priceReduction"));
    Page<ProductDTO> productsPage = productService.findAll(pageRequest).map(p -> ProductDTO.of(p, labelType));

    return ok(new ProductList(productsPage));
  }

}
