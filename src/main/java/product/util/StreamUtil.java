package product.util;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Yuriy Tumakha.
 */
public interface StreamUtil {

  /**
   * Convert Collection into Stream.
   * <p>
   * Return empty Stream if collection is null
   *
   * @param collection Collection of elements with type T
   * @param <T>
   * @return Stream of T
   */
  static <T> Stream<T> safeStream(Collection<T> collection) {
    return Stream.ofNullable(collection).flatMap(Collection::stream);
  }

}
