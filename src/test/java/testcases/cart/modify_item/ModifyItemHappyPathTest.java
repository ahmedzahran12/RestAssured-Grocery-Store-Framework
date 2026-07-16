package testcases.cart.modify_item;

import endpoints.CartEndpoint;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.cart.GetCartResponse;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;

public class ModifyItemHappyPathTest {
    @Test
    public void testModifyItemAndVerifyingSuccess(){
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        Integer myProductId = ProductService.getRandomAvailableProduct().id;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId, myProductId, 1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        GetCartResponse.Item[] items;
        int newQuantity = 2;
        cartItemRequest.setQuantity(newQuantity);
        //Act
        new CartEndpoint().modifyItemInCart(204,cartItemRequest,myCartId,addedItem.itemId);
        items = new CartEndpoint().getCartItems(200,"schemas/cart-items-schema.json",GetCartResponse.Item[].class,myCartId);
        //Assert
        Assert.assertEquals(items[0].quantity, newQuantity, "Quantity mismatch");
    }
    @Test
    public void testModifyItemWithQuantitySameAsStock(){
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        ProductResponse product = ProductService.getRandomAvailableProduct();
        Integer myProductId = product.id;
        Integer stock = ProductService.getProductById(myProductId).currentStock;
        CartItemResponse addedItem = CartService.AddProductAndGetItem(myCartId, myProductId, 1);
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(stock);
        GetCartResponse.Item[] items;
        //Act
        new CartEndpoint().modifyItemInCart(204,cartItemRequest,myCartId,addedItem.itemId);
        items = new CartEndpoint().getCartItems(200,"schemas/cart-items-schema.json",GetCartResponse.Item[].class,myCartId);
        //Assert
        Assert.assertEquals(items[0].quantity, stock, "Quantity mismatch");
    }

}
