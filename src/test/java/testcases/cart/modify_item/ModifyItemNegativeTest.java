package testcases.cart.modify_item;

import endpoints.CartEndpoint;
import models.ErrorResponse;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;

public class ModifyItemNegativeTest {
    @Test
    public void testModifyItemWithInvalidItemId(){
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(2);
        ErrorResponse errorResponse;
        Integer invalidItemId = -1;
        //Act
        errorResponse = new CartEndpoint().modifyItemInCart(404,cartItemRequest, ErrorResponse.class,myCartId,invalidItemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"No item with id " + invalidItemId+ " in this cart.");
    }
    @Test
    public void testModifyItemWithInvalidCartId(){
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        Integer productId = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId,productId,2);
        String invalidCartId = "-1";
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(3);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().modifyItemInCart(404,cartItemRequest,ErrorResponse.class,invalidCartId,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"No cart with id " + invalidCartId + ".");
    }
    @Test
    public void testModifyItemWithQuantityExceedingStock(){
        String myCartId = CartService.createNewCartAndGetId();
        ProductResponse product = ProductService.getRandomAvailableProduct();
        Integer productId = product.id;
        Integer stock = ProductService.getProductById(productId).currentStock;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId,productId,2);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(stock + 1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().modifyItemInCart(400,cartItemRequest,ErrorResponse.class,myCartId,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"The quantity requested is not available in stock.");
    }
    @Test
    public void testModifyItemWithQuantityEqualToZero(){
        String myCartId = CartService.createNewCartAndGetId();
        Integer productId = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId,productId,2);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(0);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().modifyItemInCart(400,cartItemRequest,ErrorResponse.class,myCartId,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"Invalid or missing quantity.");
    }
    @Test
    public void testModifyItemWithQuantityLessThanZero(){
        String myCartId = CartService.createNewCartAndGetId();
        Integer productId = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId,productId,2);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(-1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().modifyItemInCart(400,cartItemRequest,ErrorResponse.class,myCartId,addedItem.itemId);
        Assert.assertEquals(errorResponse.getErrorMessage(),"Invalid or missing quantity.");
    }

}
