package testcases.order.get_all_orders;

import io.restassured.response.Response;
import models.ErrorResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;

public class GetAllOrdersNegativeTest {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Test
    public void testGetAllOrdersWithInvalidToken() {
        // Arrange
        String invalidToken = "this-is-not-a-valid-token";
        // Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .header("Authorization", "Bearer " + invalidToken)
                .when()
                .get(ORDERS_ENDPOINT)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null for invalid token");
    }

    @Test
    public void testGetAllOrdersWithNoAuthorizationHeader() {
        // Arrange & Act
        Response response = given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .when()
                .get(ORDERS_ENDPOINT)
                .then()
                .statusCode(401)
                .extract().response();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        // Assert
        Assert.assertNotNull(errorResponse.getErrorMessage(), "Error message should not be null when Authorization header is missing");
    }
}
