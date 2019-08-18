package product.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import product.domain.Product;

/**
 * @author Yuriy Tumakha
 */
@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
}
