package product.dto;

import lombok.Value;
import product.domain.ProductColor;

/**
 * @author Yuriy Tumakha
 */
@Value
public class ColorSwatchDTO {

  private String color;

  private String rgbColor;

  private String skuid;

  public static ColorSwatchDTO of(ProductColor pc) {
    return new ColorSwatchDTO(pc.getColor(), pc.getRgbColor(), pc.getSkuId());
  }

}
