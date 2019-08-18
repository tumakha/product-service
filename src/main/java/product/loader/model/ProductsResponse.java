package product.loader.model;

import lombok.Data;

import java.util.List;

/**
 * @author Yuriy Tumakha
 */
@Data
public class ProductsResponse {

  private List<ProductData> products;

  private Integer results;

  private Integer pagesAvailable;

}
