package endpoints;

import models.ErrorResponse;
import models.product.ProductCategory;
import models.product.ProductQueryParams;
import models.product.ProductResponse;
import utils.RestHelper;

import java.util.Map;

public class ProductEndpoint {
    private final String productEndpoint = "/products";

    public <T> T getAllProducts(Integer statusCode,String schemaPath,Class<T> responseClass){
        return RestHelper.get(productEndpoint, responseClass,  statusCode,schemaPath);
    }
    public <T> T getAllProductsWithQuery(Integer statusCode, ProductQueryParams params,String schemaPath,Class<T> responseClass){
        return RestHelper.get(productEndpoint, responseClass, statusCode, params,schemaPath);
    }
    public <T> T getAllProductsWithQuery(Integer statusCode, Map<String,Object> params, String schemaPath, Class<T> responseClass){
        return RestHelper.get(productEndpoint, responseClass, statusCode, params,schemaPath);
    }
    public <T> T getProductById(Integer id, Integer statusCode,String schemaPath,Class<T> responseClass){
        return RestHelper.get(productEndpoint, id, responseClass, statusCode,schemaPath);
    }

}
