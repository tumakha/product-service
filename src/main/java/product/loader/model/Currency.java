package product.loader.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Tumakha
 */
public enum Currency {

  GBP, EUR, USD, AUD, NZD, CAD, HKD, SGD, DKK, NOK, ZAR, SEK, CHF, UNKNOWN_CURRENCY;

  private static final Logger LOG = LoggerFactory.getLogger(Currency.class);

  public static Currency of(String strCurrency) {
    try {
      return Currency.valueOf(strCurrency.toUpperCase());
    } catch (Exception e) {
      LOG.warn("Unknown currency '{}'", strCurrency);
      return UNKNOWN_CURRENCY;
    }
  }

}
