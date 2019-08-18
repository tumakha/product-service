package product.dto;

import lombok.Value;
import product.domain.Product;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static product.dto.PriceLabelType.ShowPercDscount;
import static product.dto.PriceLabelType.ShowWasThenNow;
import static product.util.StreamUtil.safeStream;

/**
 * @author Yuriy Tumakha
 */
@Value
public class ProductDTO {

  private String productId;

  private String title;

  private List<ColorSwatchDTO> colorSwatches;

  private String nowPrice;

  private String priceLabel;

  public static ProductDTO of(Product p, PriceLabelType labelType) {
    String priceLabel = p.getShowWasNow();

    if (labelType == ShowWasThenNow)
      priceLabel = p.getShowWasThenNow();
    else if (labelType == ShowPercDscount)
      priceLabel = p.getShowPercDscount();

    List<ColorSwatchDTO> colorSwatches = safeStream(p.getColors())
        .map(ColorSwatchDTO::of).collect(toList());

    return new ProductDTO(p.getProductId(), p.getTitle(), colorSwatches, p.getNowPriceText(), priceLabel);
  }

}
