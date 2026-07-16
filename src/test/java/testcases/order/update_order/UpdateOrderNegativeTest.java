package testcases.order.update_order;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import models.ErrorResponse;
import models.order.CreateOrderRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.OrderService;
import services.RegisterService;
import services.TokenService;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;

public class UpdateOrderNegativeTest {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Test
    public void testUpdateOrderWithInvalidOrderId() {
        // Arrange
        String token = TokenService.getToken();
        String invalidOrderId = "non-existent-order-id-xyz";
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setCustomerName(new Faker().name().fullName());
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + invalidOrderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when updating with an invalid order ID");
    }

    @Test
    public void testUpdateOrderWithInvalidToken() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        String invalidToken = "invalid-bearer-token";
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setCustomerName(new Faker().name().fullName());
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + invalidToken)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when using an invalid token");
    }

    @Test
    public void testUpdateOrderWithNoAuthorizationHeader() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setCustomerName(new Faker().name().fullName());
        // Act - no Authorization header
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when Authorization header is missing");
    }

    @Test
    public void testUpdateOrderFromDifferentApiClientReturnsNotFound() {
        // Arrange - create an order with one client, then try to update it with another client's token
        String token1 = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token1);

        // Register a second API client
        String token2 = RegisterService.registerClientAndGetToken();
        CreateOrderRequest updateRequest = new CreateOrderRequest();
        updateRequest.setCustomerName(new Faker().name().fullName());

        // Act - try to update token1's order using token2
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token2)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .patch(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when trying to update another client's order");
    }
}
