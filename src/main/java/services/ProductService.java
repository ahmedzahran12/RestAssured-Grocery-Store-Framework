package services;

import endpoints.ProductEndpoint;
import models.product.ProductQueryParams;
import models.product.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProductService {


    private static ProductResponse[] cachedProducts = null;

    private static synchronized void initializeCacheIfEmpty() {
        if (cachedProducts == null) {
            System.out.println("Fetching products from API to build local cache...");
            cachedProducts = new ProductEndpoint().getAllProducts(200, "schemas/product-list-schema.json", ProductResponse[].class);
        }
    }

    public static ProductResponse getRandomAvailableProduct() {
        initializeCacheIfEmpty();
        
        List<ProductResponse> availableProducts = new ArrayList<>();
        for (ProductResponse p : cachedProducts) {
            if (p.inStock != null && p.inStock) {
                availableProducts.add(p);
            }
        }
        
        int randomIndex = ThreadLocalRandom.current().nextInt(availableProducts.size());
        Integer selectedId = availableProducts.get(randomIndex).id;
        System.out.println("///////////Test Data///////////////");
        System.out.println("Selected random available product ID from cache: " + selectedId);
        return availableProducts.get(randomIndex);
    }

    public static ProductResponse getRandomNonAvailableProduct() {
        initializeCacheIfEmpty();
        
        List<ProductResponse> nonAvailableProducts = new ArrayList<>();
        for (ProductResponse p : cachedProducts) {
            if (p.inStock == null || !p.inStock) {
                nonAvailableProducts.add(p);
            }
        }
        
        int randomIndex = ThreadLocalRandom.current().nextInt(nonAvailableProducts.size());
        Integer selectedId = nonAvailableProducts.get(randomIndex).id;
        System.out.println("///////////Test Data///////////////");
        System.out.println("Selected random non-available product ID from cache: " + selectedId);
        return nonAvailableProducts.get(randomIndex);
    }

    public static ProductResponse[] getAllProducts(){
        initializeCacheIfEmpty();
        return cachedProducts;
    }
    public static Integer getRandomQuantity(ProductResponse product){
         Integer currentStock = product.currentStock;
         return currentStock == 1 ? 1: ThreadLocalRandom.current().nextInt(1, currentStock + 1);
    }

    public static ProductResponse getProductById(Integer productId){
        return new ProductEndpoint().getProductById(productId, 200, "schemas/product-schema.json", ProductResponse.class);
    }
}
