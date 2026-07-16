package testcases.cart.delete_item;

import endpoints.CartEndpoint;
import models.ErrorResponse;
import models.cart.CartItemResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;
import io.restassured.response.Response;

public class DeleteItemNegativeTest {

    @Test
    public void testDeleteItemWithInvalidCartId() {
        // Arrange
        String invalidCartId = "invalid-cart-id";
        Integer itemId = 12345;

        // Act
        Response deleteResponse = new CartEndpoint().deleteItemInCart(invalidCartId, itemId, 404);
        ErrorResponse errorResponse = deleteResponse.as(ErrorResponse.class);

        // Assert
        Assert.assertEquals(errorResponse.getErrorMessage(), "No cart with id " + invalidCartId + ".");
    }

    @Test
    public void testDeleteItemWithInvalidItemId() {
        // Arrange
        String myCartId = CartService.createNewCartAndGetId();
        Integer invalidItemId = -1; // Assume this item doesn't exist in cart

        // Act
        Response deleteResponse = new CartEndpoint().deleteItemInCart(myCartId, invalidItemId, 404);
        ErrorResponse errorResponse = deleteResponse.as(ErrorResponse.class);

        // Assert
        Assert.assertEquals(errorResponse.getErrorMessage(), "No item with id " + invalidItemId + ".");
    }
}
