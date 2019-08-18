package product.domain;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * @author Yuriy Tumakha
 */
@Data
@Entity
@Table(name = "color_swatch")
public class ProductColor {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_id", updatable = false, nullable = false)
  private Product product;

  private String color;

  private String basicColor;

  private String rgbColor;

  private String skuId;

}
