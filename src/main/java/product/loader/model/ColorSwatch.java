package product.loader.model;

import lombok.Data;
import product.domain.Product;
import product.domain.ProductColor;

/**
 * @author Yuriy Tumakha
 */
@Data
public class ColorSwatch {

  private String color;
  private String basicColor;
  private String skuId;

  public ProductColor toProductColor(Product product) {
    ProductColor productColor = new ProductColor();
    productColor.setProduct(product);
    productColor.setColor(color);
    productColor.setBasicColor(basicColor);
    productColor.setRgbColor(RGBColor.of(basicColor).getRGB());
    productColor.setSkuId(skuId);
    return productColor;
  }

}
