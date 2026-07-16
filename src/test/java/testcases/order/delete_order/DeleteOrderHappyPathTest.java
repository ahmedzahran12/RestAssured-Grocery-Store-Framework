package testcases.order.delete_order;

import endpoints.OrdersEndpoint;
import io.restassured.response.Response;
import models.ErrorResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.OrderService;
import services.TokenService;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;

public class DeleteOrderHappyPathTest {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Test
    public void testDeleteOrderSuccessfully() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        // Act
        given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(204);
        // Assert - verify that attempting to retrieve the deleted order returns 404
        Response getResponse = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(404)
                .extract().response();
        Assert.assertNotNull(getResponse, "Response should not be null after deletion");
    }

    @Test
    public void testDeleteOrderAndVerifyItNoLongerExists() {
        // Arrange
        String token = TokenService.getToken();
        String cartId = CartService.createNewCartAndGetId();
        CartService.addRandomItemToCart(cartId);
        String orderId = new OrderService().createOrderAndGetId(cartId, token);
        // Act - delete the order
        given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(204);
        // Assert - verify the order can no longer be retrieved
        Response getResponse = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ORDERS_ENDPOINT + "/" + orderId)
                .then()
                .statusCode(404)
                .extract().response();
        ErrorResponse errorResponse = getResponse.as(ErrorResponse.class);
        Assert.assertNotNull(errorResponse.getErrorMessage(),
                "Deleted order should return 404 with an error message when accessed");
    }
}
