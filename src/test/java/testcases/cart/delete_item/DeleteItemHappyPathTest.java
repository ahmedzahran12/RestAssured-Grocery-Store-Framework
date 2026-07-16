package testcases.cart.delete_item;

import endpoints.CartEndpoint;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.cart.GetCartResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;
import io.restassured.response.Response;

public class DeleteItemHappyPathTest {

    @Test
    public void testDeleteItemFromCart() {
        // Arrange
        String myCartId = CartService.createNewCartAndGetId();
        Integer myProductId = ProductService.getRandomAvailableProduct().id;
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(myProductId).setQuantity(1);
        CartItemResponse cartItemResponse = new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class, myCartId);
        Integer itemId = cartItemResponse.itemId;

        // Act
        Response deleteResponse = new CartEndpoint().deleteItemInCart(myCartId, itemId, 204);

        
        // Assert
        GetCartResponse.Item[] items = new CartEndpoint().getCartItems(200, "schemas/cart-items-schema.json", GetCartResponse.Item[].class, myCartId);
        boolean itemFound = false;
        for (GetCartResponse.Item item : items) {
            if (item.id.equals(itemId)) {
                itemFound = true;
                break;
            }
        }
        Assert.assertFalse(itemFound, "Expected item to be deleted from the cart");
    }
}
