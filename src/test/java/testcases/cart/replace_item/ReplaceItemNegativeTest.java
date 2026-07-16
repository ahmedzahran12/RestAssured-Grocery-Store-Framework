package testcases.cart.replace_item;

import endpoints.CartEndpoint;
import models.ErrorResponse;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.product.ProductResponse;
import org.checkerframework.checker.units.qual.C;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;

public class ReplaceItemNegativeTest {
    @Test
    public void testReplaceItemWithInvalidProductId(){
        //Arrange
        String myCart = CartService.createNewCartAndGetId();
        Integer productId = ProductService.getRandomAvailableProduct().id;
        Integer invalidProductId = -1;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(invalidProductId).setQuantity(1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(400,cartItemRequest,ErrorResponse.class,myCart,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"Invalid or missing productId.");
    }
    @Test
    public void testReplaceItemWithQuantityNegative(){
        String myCart = CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        Integer productId2 = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(-1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(400,cartItemRequest,ErrorResponse.class,myCart,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"Invalid or missing quantity.");
    }
    @Test
    public void testReplaceItemWithQuantityZero(){
        String myCart = CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        Integer productId2 = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(0);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(400,cartItemRequest,ErrorResponse.class,myCart,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"Invalid or missing quantity.");
    }
    @Test
    public void testReplaceItemWithProductNonAvailable(){
        //Arrange
        String myCart =CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        Integer productId2 = ProductService.getRandomNonAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(400,cartItemRequest,ErrorResponse.class,myCart,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"The quantity requested is not available in stock.");
    }
    @Test
    public void testReplaceItemWithQuantityExceedingStock(){
        String myCart =CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        ProductResponse product2 = ProductService.getRandomAvailableProduct();
        Integer productId2 = product2.id;
        Integer stock = ProductService.getProductById(productId2).currentStock;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(stock + 1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(400,cartItemRequest,ErrorResponse.class,myCart,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"The quantity requested is not available in stock.");

    }
    @Test
    public void testReplaceItemWithInvalidItemId(){
        String myCart = CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        Integer productId2 = ProductService.getRandomNonAvailableProduct().id;
        Integer invalidItemId = -1;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(404,cartItemRequest,ErrorResponse.class,myCart,invalidItemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"No item with id " + invalidItemId + " in this cart.");
    }
    @Test
    public void testReplaceItemWithInvalidCartId(){
        String myCart = CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        Integer productId2 = ProductService.getRandomNonAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCart,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(1);
        String invalidCartId = "-1";
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().replaceItemInCart(404,cartItemRequest,ErrorResponse.class,invalidCartId,addedItem.itemId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"No cart with id " +invalidCartId + ".");
    }

}
