package product.loader.model;

import lombok.Data;
import product.domain.Product;
import product.domain.ProductColor;
import product.loader.PriceFormatter;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static product.util.StreamUtil.safeStream;

/**
 * @author Yuriy Tumakha
 */
@Data
public class ProductData {

  private String productId;
  private String title;
  private ProductPrice price;
  private List<ColorSwatch> colorSwatches;

  public Product toProduct(PriceFormatter priceFormatter) {
    Product product = new Product();
    product.setProductId(productId);
    product.setTitle(title);

    if (price != null) {
      product.setPriceWas(toBigDecimal(price.getWas()));
      product.setPriceThen1(toBigDecimal(price.getThen1()));
      product.setPriceThen2(toBigDecimal(price.getThen2()));
      product.setPriceNow(toBigDecimal(price.getNow()));
      product.setCurrency(price.getCurrency());
      product.setPriceReduction(getPriceReduction(product));
    }

    List<ProductColor> colors = safeStream(colorSwatches)
        .map(c -> c.toProductColor(product)).collect(toList());
    product.setColors(colors);

    if (product.getPriceReduction().doubleValue() > 0) {
      String currency = product.getCurrency();
      String now = priceFormatter.format(product.getPriceNow(), currency);
      String was = priceFormatter.format(product.getPriceWas(), currency);

      String then = ofNullable(product.getPriceThen2())
          .or(() -> ofNullable(product.getPriceThen1()))
          .map(p -> String.format(" then %s,", priceFormatter.format(p, currency)))
          .orElse("");;

      int percent = product.getPriceReduction().multiply(BigDecimal.valueOf(100))
          .divide(product.getPriceWas(), HALF_UP)
          .setScale(0, HALF_UP).intValue();

      product.setNowPriceText(now);
      product.setShowWasNow(String.format("Was %s, now %s", was, now));
      product.setShowWasThenNow(String.format("Was %s,%s now %s", was, then, now));
      product.setShowPercDscount(String.format("%d%% off - now %s", percent, now));
    }
    return product;
  }

  private BigDecimal getPriceReduction(Product product) {
    if (product.getPriceNow() != null && product.getPriceWas() != null)
      return product.getPriceWas().subtract(product.getPriceNow());
    else
      return BigDecimal.ZERO;
  }

  private BigDecimal toBigDecimal(Object obj) {
    return ofNullable(obj)
        .filter(o -> o instanceof String)
        .map(Object::toString)
        .filter(s -> !s.isEmpty())
        .map(BigDecimal::new)
        .orElse(null);
  }

}
