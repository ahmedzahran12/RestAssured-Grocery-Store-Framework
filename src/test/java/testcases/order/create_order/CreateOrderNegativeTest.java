package testcases.order.create_order;

import com.github.javafaker.Faker;
import endpoints.OrdersEndpoint;
import models.ErrorResponse;
import models.order.CreateOrderRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.TokenService;

public class CreateOrderNegativeTest {

    @Test
    public void testCreateOrderWithoutToken() {
        // Arrange
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(new Faker().name().fullName());
        // Act
        ErrorResponse errorResponse = new OrdersEndpoint().createOrder(401, ErrorResponse.class, request, "invalid-token");
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null for unauthorized request");
    }

    @Test
    public void testCreateOrderWithMissingCartId() {
        // Arrange
        String token = TokenService.getToken();
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName(new Faker().name().fullName());
        // cartId is intentionally not set (null)
        // Act
        ErrorResponse errorResponse = new OrdersEndpoint().createOrder(400, ErrorResponse.class, request, token);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null when cartId is missing");
    }

    @Test
    public void testCreateOrderWithMissingCustomerName() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId);
        // customerName is intentionally not set (null)
        // Act
        ErrorResponse errorResponse = new OrdersEndpoint().createOrder(400, ErrorResponse.class, request, token);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null when customerName is missing");
    }

    @Test
    public void testCreateOrderWithInvalidCartId() {
        // Arrange
        String token = TokenService.getToken();
        String invalidCartId = "invalid-cart-id-xyz";
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(invalidCartId).setCustomerName(new Faker().name().fullName());
        // Act
        ErrorResponse errorResponse = new OrdersEndpoint().createOrder(400, ErrorResponse.class, request, token);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null for invalid cartId");
    }

    @Test
    public void testCreateOrderWithEmptyCart() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        // No items added to cart
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(new Faker().name().fullName());
        // Act
        ErrorResponse errorResponse = new OrdersEndpoint().createOrder(400, ErrorResponse.class, request, token);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null when ordering with an empty cart");
    }
}
