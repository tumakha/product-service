package product.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

/**
 * @author Yuriy Tumakha
 */
@Data
@Entity
@Table(name = "product")
public class Product {

  @Id
  private String productId;

  private String title;

  @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
  private List<ProductColor> colors;

  private BigDecimal priceWas;
  private BigDecimal priceThen1;
  private BigDecimal priceThen2;
  private BigDecimal priceNow;
  private BigDecimal priceReduction;
  private String currency;

  private String nowPriceText;
  private String ShowWasNow;
  private String ShowWasThenNow;
  private String ShowPercDscount;

}
