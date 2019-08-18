package product.loader;

import org.junit.Test;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Yuriy Tumakha
 */
public class PriceFormatterTest {

  private PriceFormatter priceFormatter = new PriceFormatter();

  @Test
  public void testFormatPrice() {
    assertThat(priceFormatter.format(valueOf(1.2345), "GBP"), equalTo("£1.23"));
    assertThat(priceFormatter.format(valueOf(13.8), "EUR"), equalTo("13,80 €"));
    assertThat(priceFormatter.format(valueOf(2.5), "USD"), equalTo("$2.50"));
    assertThat(priceFormatter.format(valueOf(22111.5), "AUD"), equalTo("$22,111.50"));
    assertThat(priceFormatter.format(valueOf(78333.99), "NZD"), equalTo("$78,333.99"));
    assertThat(priceFormatter.format(valueOf(1200777), "CAD"), equalTo("$1,200,777"));
    assertThat(priceFormatter.format(valueOf(200555), "HKD"), equalTo("$200,555"));
    assertThat(priceFormatter.format(valueOf(555200), "SGD"), equalTo("$555,200"));
    assertThat(priceFormatter.format(valueOf(1400999.22), "DKK"), equalTo("1.400.999,22 kr"));
    assertThat(priceFormatter.format(valueOf(2800999), "NOK"), equalTo("2.800.999,00 kr"));
    assertThat(priceFormatter.format(valueOf(7200), "ZAR"), equalTo("R7,200"));
    assertThat(priceFormatter.format(valueOf(8440.24), "SEK"), equalTo("SEK 8,440.24"));
    assertThat(priceFormatter.format(valueOf(8333.99), "CHF"), equalTo("₣8,333.99"));
  }

  @Test
  public void testFormatPriceForIntegers() {
    assertThat(priceFormatter.format(valueOf(9), "GBP"), equalTo("£9.00"));
    assertThat(priceFormatter.format(valueOf(10), "GBP"), equalTo("£10"));
  }

  @Test
  public void testFormatUnknownCurrency() {
    assertThat(priceFormatter.format(valueOf(1.99), "ZZZ"), equalTo("1.99"));
    assertThat(priceFormatter.format(valueOf(10500.99), "ZZZ"), equalTo("10,500.99"));
  }

  @Test
  public void testFormatNullCurrency() {
    assertThat(priceFormatter.format(valueOf(11.99), null), equalTo("11.99"));
  }

}
