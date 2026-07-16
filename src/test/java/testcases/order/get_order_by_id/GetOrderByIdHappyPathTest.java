package testcases.order.get_order_by_id;

import com.github.javafaker.Faker;
import endpoints.OrdersEndpoint;
import models.cart.CartItem;
import models.order.CreateOrderRequest;
import models.order.CreateOrderResponse;
import models.order.GetOrder;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.OrderService;
import services.TokenService;

public class GetOrderByIdHappyPathTest {

    @Test
    public void testGetOrderByIdReturnsCorrectOrder() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartItem addedItem = CartService.addRandomItemToCart(cartId);
        String customerName = new Faker().name().fullName();
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(customerName);
        CreateOrderResponse createResponse = new OrdersEndpoint().createOrder(201, CreateOrderResponse.class, request, token);
        String orderId = createResponse.orderId;
        // Act
        GetOrder order = new OrdersEndpoint().getOrderById(200, "schemas/order-schema.json", GetOrder.class, orderId, token);
        // Assert
        Assert.assertNotNull(order, "Order should not be null");
        Assert.assertEquals(order.id, orderId, "Returned order ID should match the created order ID");
        Assert.assertEquals(order.customerName, customerName, "Customer name should match the one used during creation");
        Assert.assertNotNull(order.created, "Created timestamp should not be null");
        Assert.assertNotNull(order.items, "Items list should not be null");
        Assert.assertFalse(order.items.isEmpty(), "Order should contain at least one item");
    }

    @Test
    public void testGetOrderByIdReturnsCorrectItems() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartItem addedItem = CartService.addRandomItemToCart(cartId);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(new Faker().name().fullName());
        CreateOrderResponse createResponse = new OrdersEndpoint().createOrder(201, CreateOrderResponse.class, request, token);
        // Act
        GetOrder order = new OrdersEndpoint().getOrderById(200, "schemas/order-schema.json", GetOrder.class, createResponse.orderId, token);
        // Assert
        Assert.assertEquals(order.items.get(0).productId, addedItem.getProductId(),
                "Product ID in order item should match the product added to the cart");
        Assert.assertEquals(order.items.get(0).quantity, addedItem.getQuantity(),
                "Quantity in order item should match the quantity added to the cart");
        Assert.assertEquals(order.items.get(0).id, addedItem.getItemId(),
                "Item ID in order should match the cart item ID");
    }

    @Test
    public void testGetOrderByIdWithCommentPreservesComment() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String customerName = new Faker().name().fullName();
        String comment = "Please deliver before noon";
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(customerName).setComment(comment);
        CreateOrderResponse createResponse = new OrdersEndpoint().createOrder(201, CreateOrderResponse.class, request, token);
        // Act
        GetOrder order = new OrdersEndpoint().getOrderById(200, "schemas/order-schema.json", GetOrder.class, createResponse.orderId, token);
        // Assert
        Assert.assertEquals(order.comment, comment, "Comment should be preserved and returned with the order");
    }
}
