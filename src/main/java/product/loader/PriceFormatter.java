package product.loader;

import org.springframework.stereotype.Component;
import product.loader.model.Currency;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static product.loader.model.Currency.*;

/**
 * @author Yuriy Tumakha
 */
@Component
public class PriceFormatter {

  private static final NumberFormat DEFAULT_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

  private Map<Currency, NumberFormat> formatter = new HashMap<>() {{
    put(GBP, NumberFormat.getCurrencyInstance(Locale.UK));
    put(EUR, NumberFormat.getCurrencyInstance(Locale.GERMANY));
    put(USD, NumberFormat.getCurrencyInstance(Locale.US));
    put(DKK, NumberFormat.getCurrencyInstance(Locale.GERMANY));
    put(NOK, NumberFormat.getCurrencyInstance(Locale.GERMANY));
  }};

  public String format(BigDecimal price, String strCurrency) {
    Currency currency = Currency.of(strCurrency);
    NumberFormat fmt = formatter.getOrDefault(currency, DEFAULT_FORMAT);
    String priceText = remove2TrailingZeros(fmt.format(price));
    switch (currency) {
      case DKK:
      case NOK:
        priceText = priceText.replace("€", "kr");
        break;
      case ZAR:
        priceText = priceText.replace('$', 'R');
        break;
      case SEK:
        priceText = priceText.replace("$", "SEK ");
        break;
      case CHF:
        priceText = priceText.replace('$', '₣');
        break;
      case UNKNOWN_CURRENCY:
        priceText = priceText.replace("$", "");
    }
    return priceText;
  }

  private String remove2TrailingZeros(String priceText) {
    if (priceText.endsWith(".00") && priceText.length() > 5)
      priceText = priceText.substring(0, priceText.length() - 3);
    return priceText;
  }

}
