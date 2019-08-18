package product.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import product.config.ProductsApiConfig;
import product.domain.Product;
import product.loader.model.ProductData;
import product.loader.model.ProductsResponse;
import product.service.ProductService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.time.Duration.ofSeconds;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static product.util.StreamUtil.safeStream;

/**
 * @author Yuriy Tumakha
 */
@Component
public class ProductsLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ProductsLoader.class);

  @Autowired
  private ProductsApiConfig config;

  @Autowired
  private JsonSupport jsonSupport;

  @Autowired
  private PriceFormatter priceFormatter;

  @Autowired
  private ProductService productService;

  private static final int threads = getRuntime().availableProcessors() * 2;
  private ExecutorService executorService = Executors.newFixedThreadPool(threads);
  private HttpClient httpClient = HttpClient.newBuilder().connectTimeout(ofSeconds(5)).build();

  private CompletableFuture<Integer> getTotalPagesFuture;

  @Scheduled(initialDelay = 1000, fixedDelay = 60_000) // 1 minute delay before next run
  public void syncProducts() {
    List<CompletableFuture<Integer>> futures = new ArrayList<>();
    getTotalPagesFuture = new CompletableFuture<>();
    int page = 1;

    do {
      HttpRequest request = newRequest(page);
      final int currentPage = page;

      CompletableFuture<Integer> future = sendWithRetry(request, this::sendRequest)
          .thenApplyAsync(response -> parseProducts(response, currentPage), executorService)
          .exceptionallyAsync(ex -> {
            LOG.error("Loading products failed", ex);
            return 0;
          }, executorService);

      futures.add(future);

    } while (page++ < getTotalPages());

    CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    allFutures.thenRunAsync(() -> {
      int pages = futures.stream().mapToInt(CompletableFuture::join).sum();
      LOG.info("Processed {} product pages", pages);
    }, executorService);
  }

  private int getTotalPages() {
    return getTotalPagesFuture.completeOnTimeout(1, 9, SECONDS).join();
  }

  private HttpRequest newRequest(int page) {
    return HttpRequest.newBuilder()
        .uri(URI.create(config.getProductsUrl(page)))
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .build();
  }

  private <T> CompletableFuture<HttpResponse<T>> sendWithRetry(
      HttpRequest request,
      Function<HttpRequest, CompletableFuture<HttpResponse<T>>> sender) {

    return sender.apply(request)
        .exceptionallyComposeAsync(ex -> {
          LOG.error("First request attempt failed " + request.uri(), ex);
          return sender.apply(request);
        }, executorService);
  }

  private CompletableFuture<HttpResponse<String>> sendRequest(HttpRequest request) {
    return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
  }

  private int parseProducts(HttpResponse<String> response, int page) {
    try {
      if (response.statusCode() != HTTP_OK) {
        LOG.error("Request page {} failed. HTTP Code {} {}", page, response.statusCode(), response.body());
        return 0;
      }

      ProductsResponse productsResponse = jsonSupport.fromJson(response.body(), ProductsResponse.class);
      if (!getTotalPagesFuture.isDone())
        getTotalPagesFuture.complete(productsResponse.getPagesAvailable());

      List<Product> products = safeStream(productsResponse.getProducts())
          .filter(this::isPriceReduced)
          .map(pd -> pd.toProduct(priceFormatter))
          .filter(p -> p.getPriceReduction().doubleValue() > 0)
          .collect(toList());

      productService.upsert(products);

      LOG.info(format("Page %3d. Loaded %2d products", page, products.size()));
      return 1;
    } catch (Exception e) {
      LOG.error("Processing JSON response failed", e);
      return 0;
    }
  }

  private boolean isPriceReduced(ProductData productData) {
    return ofNullable(productData.getPrice())
        .map(price -> price.getWas() instanceof String && !price.getWas().toString().isBlank())
        .orElse(false);
  }

}
