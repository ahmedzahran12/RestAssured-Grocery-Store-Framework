package testcases.cart.add_item;

import endpoints.CartEndpoint;
import models.ErrorResponse;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;

public class AddItemNegativeTest {
    @Test
    public void testAddingItemThatAlreadyExists() {
        String myCartId = CartService.createNewCartAndGetId();
        Integer myProductId = ProductService.getRandomAvailableProduct().id;
        CartItemRequest cartItemRequest = new CartItemRequest();
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemRequest.setProductId(myProductId).setQuantity(2);
        ErrorResponse errorResponse;
        // Act
        cartItemResponse = new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class, myCartId);
        errorResponse = new CartEndpoint().AddCartItem(400, cartItemRequest, ErrorResponse.class, myCartId);
        // Assert
        Assert.assertEquals(errorResponse.getErrorMessage(), "This product has already been added to cart.",
        "Expected correct error message for adding duplicate item");
    }

    @Test
    public void testAddingItemForProductThatDoesNotExist() {
        String myCartId = CartService.createNewCartAndGetId();
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(-1).setQuantity(2);
        ErrorResponse errorResponse;
        errorResponse = new CartEndpoint().AddCartItem(400, cartItemRequest, ErrorResponse.class, myCartId);
        // Assert
        Assert.assertEquals(errorResponse.getErrorMessage(), "Invalid or missing productId.",
        "Expected correct error message for adding item with invalid product id");
    }

    @Test
    public void testAddingItemThatIsNotAvailable()
    {
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        Integer myProductId = ProductService.getRandomNonAvailableProduct().id;
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(myProductId).setQuantity(2);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().AddCartItem(400, cartItemRequest, ErrorResponse.class, myCartId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"This product is not in stock and cannot be ordered.","Expected error message for adding item that is out of stock");

    }
    @Test
    public void testAddingItemWithQuantityExceedingStock()
    {
        //Arrange
        String myCartId = CartService.createNewCartAndGetId();
        ProductResponse product = ProductService.getRandomAvailableProduct();
        Integer myProductId = product.id;
        Integer currentStock = ProductService.getProductById(myProductId).currentStock;
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(myProductId).setQuantity(currentStock + 1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().AddCartItem(400, cartItemRequest, ErrorResponse.class, myCartId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"The quantity requested exceeds the current stock.","Expected error message for adding item with quantity exceeding stock");
    }
    @Test
    public void testAddingItemWithInvalidCartId(){
        //Arrange
        Integer myProductId = ProductService.getRandomAvailableProduct().id;
        String myCartId = "invalid-cart-id";
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(myProductId).setQuantity(1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().AddCartItem(404, cartItemRequest, ErrorResponse.class, myCartId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"No cart with id " + myCartId + ".");
    }
    @Test
    public void testAddingItemWithInvalidProductId(){
        Integer myProductId = -1;
        String myCartId = CartService.createNewCartAndGetId();
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(myProductId).setQuantity(1);
        ErrorResponse errorResponse;
        //Act
        errorResponse = new CartEndpoint().AddCartItem(400, cartItemRequest, ErrorResponse.class, myCartId);
        //Assert
        Assert.assertEquals(errorResponse.getErrorMessage(),"Invalid or missing productId.");

    }
}
