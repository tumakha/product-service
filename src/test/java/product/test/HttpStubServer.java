package product.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author Yuriy Tumakha
 */
@RestController
@RequestMapping(path = "/stub", produces = APPLICATION_JSON_UTF8_VALUE)
public class HttpStubServer implements ResourceReader {

  @Value("classpath:stub/products.json")
  private Resource productsStub;

  @GetMapping(path = "/v1/categories/600001506/products")
  public String getProducts() throws IOException {
    return getResourceAsString(productsStub);
  }

}
