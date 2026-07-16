package testcases.product;

import endpoints.ProductEndpoint;
import models.ErrorResponse;
import models.product.ProductQueryParams;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class GetProductNegativeTest {
    @Test
    public void testGetProductWithInvalidId(){
        //Arrange
        ErrorResponse response;
        Integer productId = -1;
        //Act
        response = new ProductEndpoint().getProductById(productId,404,"schemas/error-schema.json", ErrorResponse.class);
        //Assert
        Assert.assertEquals(response.getErrorMessage(),"No product with id " + productId + ".");
    }
    @Test
    public void testGetProductWithInvalidResultsParam(){
        //Arrange
        ErrorResponse response;
        ProductQueryParams queryParams = new ProductQueryParams();
        queryParams.setResults(-1);
        //Act
        response = new ProductEndpoint().getAllProductsWithQuery(400,queryParams,"schemas/error-schema.json",ErrorResponse.class);
        //Assert
        Assert.assertEquals(response.getErrorMessage(),"Invalid value for query parameter 'results'. Must be greater than 0.");

    }
    @Test
    public void testGetProductWithInvalidCategory(){
        //Arrange
        ErrorResponse response;
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("category","invalid");
        //Act
        response = new ProductEndpoint().getAllProductsWithQuery(400,queryParams,"schemas/error-schema.json",ErrorResponse.class);
        //Assert
        Assert.assertEquals(response.getErrorMessage(),"Invalid value for query parameter 'category'. Must be one of: coffee, fresh-produce, meat-seafood, candy, dairy, bread-bakery, eggs");
    }
}
