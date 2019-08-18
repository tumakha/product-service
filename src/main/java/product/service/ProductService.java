package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import product.domain.Product;
import product.repository.ProductRepository;

/**
 * @author Yuriy Tumakha
 */
@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  public void upsert(Iterable<Product> products) {
    productRepository.saveAll(products);
  }

  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

}
