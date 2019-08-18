package product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Yuriy Tumakha
 */
@EnableScheduling
@Configuration
@ConfigurationProperties("products")
@Data
public class ProductsApiConfig {

  private String url;
  private String key;

  public String getProductsUrl(int page) {
    return String.format("%s?key=%s&page=%d", url, key, page);
  }

}
