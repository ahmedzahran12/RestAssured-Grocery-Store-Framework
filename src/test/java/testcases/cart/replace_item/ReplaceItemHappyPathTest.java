package testcases.cart.replace_item;

import endpoints.CartEndpoint;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.cart.GetCartResponse;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;

public class ReplaceItemHappyPathTest {
    @Test
    public void testReplaceItemAndVerifyingSuccess(){
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        Integer productId2 = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(1);
        GetCartResponse.Item[] items;
        //Act
        new CartEndpoint().replaceItemInCart(204,cartItemRequest,myCartId,addedItem.itemId);
        items = new CartEndpoint().getCartItems(200,"schemas/cart-items-schema.json",GetCartResponse.Item[].class,myCartId);
        //Assert
        Assert.assertEquals(items[0].productId,productId2,"Expected product id to match");
        Assert.assertEquals(items[0].quantity,1,"Expected quantity to match");
    }
    @Test
    public void testReplaceItemWithQuantitySameAsStock(){
        String myCartId = CartService.createNewCartAndGetId();
        Integer productId1 = ProductService.getRandomAvailableProduct().id;
        ProductResponse product2 = ProductService.getRandomAvailableProduct();
        Integer productId2 = product2.id;
        Integer stock = ProductService.getProductById(productId2).currentStock;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId,productId1,1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId2).setQuantity(stock);
        GetCartResponse.Item[] items;
        //Act
        new CartEndpoint().replaceItemInCart(204,cartItemRequest,myCartId,addedItem.itemId);
        items = new CartEndpoint().getCartItems(200,"schemas/cart-items-schema.json",GetCartResponse.Item[].class,myCartId);
        //Assert
        Assert.assertEquals(items[0].productId,productId2,"Expected product id to match");
        Assert.assertEquals(items[0].quantity,stock,"Expected quantity to match");

    }
}
