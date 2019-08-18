package product.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Yuriy Tumakha
 */
@Value
public class ProductList {

  private Page<ProductDTO> page;

  public List<ProductDTO> getProducts() {
    return page.getContent();
  }

  public int getTotalPages() {
    return page.getTotalPages();
  }

  public long getTotalElements() {
    return page.getTotalElements();
  }

  public int getPage() {
    return page.getNumber();
  }

  public int getSize() {
    return page.getSize();
  }

}
