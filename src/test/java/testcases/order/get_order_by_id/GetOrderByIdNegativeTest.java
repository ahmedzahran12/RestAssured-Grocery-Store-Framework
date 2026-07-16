package testcases.order.get_order_by_id;

import io.restassured.response.Response;
import models.ErrorResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.OrderService;
import services.RegisterService;
import services.TokenService;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;

public class GetOrderByIdNegativeTest {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Test
    public void testGetOrderByIdWithInvalidOrderId() {
        // Arrange
        String token = TokenService.getToken();
        String invalidOrderId = "non-existent-order-id-xyz";
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ORDERS_ENDPOINT + "/" + invalidOrderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null for an invalid order ID");
    }

    @Test
    public void testGetOrderByIdWithInvalidToken() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        String invalidToken = "this-is-an-invalid-token";
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + invalidToken)
                .when()
                .get(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null for an invalid token");
    }

    @Test
    public void testGetOrderByIdWithNoAuthorizationHeader() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        // Act - no Authorization header
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .when()
                .get(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null when Authorization header is missing");
    }

    @Test
    public void testGetOrderByIdFromDifferentApiClientReturnsNotFound() {
        // Arrange - create an order with one client, then try to access it with another client's token
        String token1 = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token1);

        // Register a different API client to get a second token
        String token2 = RegisterService.registerClientAndGetToken();

        // Act - try to access token1's order with token2 (orders are scoped per client)
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token2)
                .when()
                .get(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when accessing another client's order");
    }
}
