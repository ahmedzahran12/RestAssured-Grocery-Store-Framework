package testcases.product;

import endpoints.ProductEndpoint;
import models.product.ProductCategory;
import models.product.ProductQueryParams;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GetProductsHappyPathTest {

    @Test
    public void testGetAllProducts() {
        //Arrange
        ProductResponse[] response;
        //Act
        response = new ProductEndpoint().getAllProducts(200, "schemas/product-list-schema.json",ProductResponse[].class);
        //Assert
        Assert.assertTrue(response.length > 0);
        Assert.assertNotNull(response[0].id);
    }

    @Test
    public void testGetAllAvailableProducts() {
        //Arrange
        ProductResponse[] response;
        ProductQueryParams queryParams = new ProductQueryParams();
        queryParams.setAvailable(true);
        //Act
        response = new ProductEndpoint().getAllProductsWithQuery(200, queryParams, "schemas/product-list-schema.json",ProductResponse[].class);
        //Assert
        Assert.assertTrue(response.length > 0);
        Assert.assertNotNull(response[0].id);
        for (ProductResponse product : response) {
            Assert.assertTrue(product.inStock);
        }

    }

    @Test
    public void testGetAllNonAvailableProducts() {
        //Arrange
        ProductResponse[] response;
        ProductQueryParams queryParams = new ProductQueryParams();
        queryParams.setAvailable(false);
        //Act
        response = new ProductEndpoint().getAllProductsWithQuery(200, queryParams, "schemas/product-list-schema.json",ProductResponse[].class);
        //Assert
        Assert.assertTrue(response.length > 0);
        Assert.assertNotNull(response[0].id);
        for (ProductResponse product : response) {
            Assert.assertFalse(product.inStock);
        }
    }

    @Test(dataProvider = "getCategory")
    public void testGetProductByCategory(ProductCategory category) {
        //Arrange
        ProductResponse[] response;
        ProductQueryParams queryParams = new ProductQueryParams();
        queryParams.setCategory(category);
        //Act
        response = new ProductEndpoint().getAllProductsWithQuery(200, queryParams, "schemas/product-list-schema.json",ProductResponse[].class);
        //Assert
        Assert.assertTrue(response.length > 0);
        Assert.assertNotNull(response[0].id);
        for (ProductResponse product : response) {
            Assert.assertEquals(product.category, category.getValue());
        }

    }
    @Test
    public void testGetProductByLimit() {
        //Arrange
        ProductResponse[] response;
        ProductQueryParams queryParams = new ProductQueryParams();
        queryParams.setResults(5);
        //Act
        response = new ProductEndpoint().getAllProductsWithQuery(200, queryParams, "schemas/product-list-schema.json",ProductResponse[].class);
        //Assert
        Assert.assertEquals(response.length, 5);
        Assert.assertNotNull(response[0].id);

    }
    @Test
    public void testGetProductById(){
        //Arrange
        ProductResponse response;
        Integer productId = 4643;
        //Act
        response = new ProductEndpoint().getProductById(productId,200,"schemas/product-schema.json",ProductResponse.class);
        //Assert
        Assert.assertEquals(response.id,productId);
    }

    @DataProvider
    public Object[][] getCategory() {
        ProductCategory[] categories = ProductCategory.values();
        Object[][] data = new Object[categories.length][1];
        for (int i = 0; i < categories.length; i++) {
            data[i][0] = categories[i];
        }
        return data;
    }
}
