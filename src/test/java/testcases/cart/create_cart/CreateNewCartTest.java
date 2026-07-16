package testcases.cart.create_cart;

import endpoints.CartEndpoint;
import models.cart.CreateCartResponse;
import models.cart.GetCartResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateNewCartTest {
    @Test
    public void testCreatingNewCartAndVerifyItExists(){
        //Arrange
        CreateCartResponse createCartResponse;
        GetCartResponse getCartResponse;
        //Act
        createCartResponse = new CartEndpoint().createNewCart(201, "schemas/cart-created-schema.json");
        getCartResponse = new CartEndpoint().getCart(200,"schemas/cart-schema.json",GetCartResponse.class,createCartResponse.cartId);
        //Assert
        Assert.assertNotNull(createCartResponse.cartId);
        Assert.assertTrue(createCartResponse.created);
        Assert.assertEquals(getCartResponse.items.size(),0);

    }
}
