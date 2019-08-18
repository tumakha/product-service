package product.loader.model;

import lombok.Data;

/**
 * @author Yuriy Tumakha
 */
@Data
public class ProductPrice {

  private Object was;
  private Object then1;
  private Object then2;
  private Object now;
  private String currency;

}
