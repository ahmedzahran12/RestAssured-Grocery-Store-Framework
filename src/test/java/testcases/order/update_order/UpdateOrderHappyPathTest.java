package testcases.order.update_order;

import com.github.javafaker.Faker;
import endpoints.OrdersEndpoint;
import io.restassured.response.Response;
import models.order.CreateOrderRequest;
import models.order.GetOrder;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.OrderService;
import services.TokenService;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;

public class UpdateOrderHappyPathTest {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Test
    public void testUpdateOrderCustomerNameSuccessfully() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        String newCustomerName = Faker.instance().name().fullName();
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setCustomerName(newCustomerName);
        // Act - PATCH returns 204 No Content
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(204)
                .extract().response();
        // Assert - verify the change by fetching the order
        GetOrder updatedOrder = new OrdersEndpoint().getOrderById(200, "schemas/order-schema.json", GetOrder.class, orderId, token);
        Assert.assertEquals(updatedOrder.customerName, newCustomerName,
                "Customer name should be updated to the new value");
    }

    @Test
    public void testUpdateOrderCommentSuccessfully() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        String newComment = "Leave at the front door";
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setComment(newComment);
        // Act
        given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(204);
        // Assert - verify comment was saved
        GetOrder updatedOrder = new OrdersEndpoint().getOrderById(200, "schemas/order-schema.json", GetOrder.class, orderId, token);
        Assert.assertEquals(updatedOrder.comment, newComment,
                "Comment should be updated to the new value");
    }

    @Test
    public void testUpdateOrderBothFieldsSuccessfully() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        String newCustomerName = Faker.instance().name().fullName();
        String newComment = "Ring the bell twice";
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setCustomerName(newCustomerName).setComment(newComment);
        // Act
        given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(204);
        // Assert
        GetOrder updatedOrder = new OrdersEndpoint().getOrderById(200, "schemas/order-schema.json", GetOrder.class, orderId, token);
        Assert.assertEquals(updatedOrder.customerName, newCustomerName,
                "Customer name should reflect the update");
        Assert.assertEquals(updatedOrder.comment, newComment,
                "Comment should reflect the update");
    }
}
