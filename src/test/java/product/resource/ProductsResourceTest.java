package product.resource;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import product.repository.ProductRepository;
import product.test.ResourceReader;

import java.io.IOException;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.data.domain.PageRequest.of;

/**
 * @author Yuriy Tumakha
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class ProductsResourceTest implements ResourceReader {

  @LocalServerPort
  private int webPort;

  @Value("classpath:products-was-now.json")
  private Resource productsWasNow;

  @Value("classpath:products-was-then-now.json")
  private Resource productsWasThenNow;

  @Value("classpath:products-percent-discount.json")
  private Resource productsPercentDiscount;

  @Autowired
  private ProductRepository productRepository;

  private boolean isEmptyProductsTable() {
    return productRepository.findAll(of(0, 9)).getTotalElements() == 0;
  }

  @Before
  public void setUp() throws InterruptedException {
    RestAssured.port = webPort;

    for (int i = 0; isEmptyProductsTable() && i < 5; i++) {
      SECONDS.sleep(1);
    }
  }

  @Test
  public void testGetProducts() throws IOException {
    String response = when().
        get("/v1/products").
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(response).isEqualTo(getResourceAsString(productsWasNow));
  }

  @Test
  public void testGetProductsShowWasThenNow() throws IOException {
    String response = when().
        get("/v1/products?labelType=ShowWasThenNow").
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(response).isEqualTo(getResourceAsString(productsWasThenNow));
  }

  @Test
  public void testGetProductsShowPercentDiscount() throws IOException {
    String response = when().
        get("/v1/products?labelType=ShowPercDscount&page=0&size=100").
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(response).isEqualTo(getResourceAsString(productsPercentDiscount));
  }

  @Test
  public void testGetProductsInvalidPriceLabelType() {
    when().
        get("/v1/products?labelType=WrongType").
        then().
        statusCode(HTTP_BAD_REQUEST).
        contentType(JSON).
        body("message", containsString("Failed to convert value of type 'java.lang.String' to required type 'product.dto.PriceLabelType'"),
            "error", equalTo("Bad Request"));
  }

  @Test
  public void testGetProductsInvalidPageNumber() {
    when().
        get("/v1/products?page=-1&size=100").
        then().
        statusCode(HTTP_INTERNAL_ERROR).
        contentType(JSON).
        body("message", equalTo("getProducts.page: must be greater than or equal to 0"),
            "error", equalTo("Internal Server Error"));
  }

  @Test
  public void testUnknownPath() {
    when().
        get("/v1/unknown/path").
        then().
        statusCode(HTTP_NOT_FOUND).
        contentType(JSON).
        body("error", equalTo("Not Found"));
  }

}
