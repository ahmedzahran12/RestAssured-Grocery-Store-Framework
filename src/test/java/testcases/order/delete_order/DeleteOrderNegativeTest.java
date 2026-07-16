package testcases.order.delete_order;

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

public class DeleteOrderNegativeTest {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Test
    public void testDeleteOrderWithInvalidOrderId() {
        // Arrange
        String token = TokenService.getToken();
        String invalidOrderId = "non-existent-order-id-xyz";
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + invalidOrderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when deleting an invalid order ID");
    }

    @Test
    public void testDeleteOrderWithInvalidToken() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        String invalidToken = "invalid-bearer-token";
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + invalidToken)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when deleting with an invalid token");
    }

    @Test
    public void testDeleteOrderWithNoAuthorizationHeader() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        // Act - no Authorization header
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when Authorization header is missing");
    }

    @Test
    public void testDeleteAlreadyDeletedOrderReturnsNotFound() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        // Delete order the first time
        given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(204);
        // Act - attempt to delete the same order again
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when deleting an already-deleted order");
    }

    @Test
    public void testDeleteOrderFromDifferentApiClientReturnsNotFound() {
        // Arrange - create an order with one client, then try to delete it with another client's token
        String token1 = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token1);

        // Register a second API client
        String token2 = RegisterService.registerClientAndGetToken();

        // Act - try to delete token1's order using token2
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token2)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Error message should not be null when trying to delete another client's order");
    }
}
