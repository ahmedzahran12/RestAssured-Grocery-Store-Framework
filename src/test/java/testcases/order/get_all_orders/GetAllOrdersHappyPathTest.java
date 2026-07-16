package testcases.order.get_all_orders;

import endpoints.OrdersEndpoint;
import models.order.GetOrder;
import models.order.CreateOrderRequest;
import models.order.CreateOrderResponse;
import models.ErrorResponse;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.TokenService;

import java.util.List;

public class GetAllOrdersHappyPathTest {

    @Test
    public void testGetAllOrdersReturnsSuccessfully() {
        // Arrange
        String token = TokenService.getToken();
        // Create at least one order to ensure the list is non-empty
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(new Faker().name().fullName());
        new OrdersEndpoint().createOrder(201, CreateOrderResponse.class, request, token);
        // Act
        List<GetOrder> orders = new OrdersEndpoint().getAllOrders(200, "schemas/all-orders-schema.json", List.class, token);
        // Assert
        Assert.assertNotNull(orders, "Orders list should not be null");
        Assert.assertFalse(orders.isEmpty(), "Orders list should contain at least one order");
    }

    @Test
    public void testGetAllOrdersContainsCreatedOrder() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String customerName = new Faker().name().fullName();
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId).setCustomerName(customerName);
        CreateOrderResponse createResponse = new OrdersEndpoint().createOrder(201, CreateOrderResponse.class, request, token);
        String createdOrderId = createResponse.orderId;
        // Act
        GetOrder[] orders = new OrdersEndpoint().getAllOrders(200, "schemas/all-orders-schema.json", GetOrder[].class, token);
        // Assert
        boolean orderFound = false;
        for (GetOrder order : orders) {
            if (order.id.equals(createdOrderId)) {
                orderFound = true;
                Assert.assertEquals(order.customerName, customerName, "Customer name in order list should match the one used during creation");
                break;
            }
        }
        Assert.assertTrue(orderFound, "Newly created order should appear in the list of all orders");
    }
}
