package utils;

import io.restassured.response.Response;
import models.product.ProductQueryParams;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.restassured.http.ContentType;

import io.restassured.RestAssured;

public class RestHelper {

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T get(String endpoint, Object id, Class<T> responseClass, Integer statusCode,
            String schemaPath) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .when()
                .get(endpoint + "/" + id)
                .then()
                .statusCode(statusCode)
                .assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
                .extract().response().as(responseClass);
    }

    public static <T> T get(String endpoint, Class<T> responseClass, Integer statusCode, String schemaPath) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .when()
                .get(endpoint)
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }
    public static <T> T get(String endpoint, Class<T> responseClass, Integer statusCode, String schemaPath,String token) {
        return given()
        .baseUri(ConfigLoader.getProperty("baseUrl"))
        .header("Authorization", "Bearer " + token)
        .when()
        .get(endpoint)
        .then()
        .assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
        .statusCode(statusCode)
        .extract().response().as(responseClass);
    }

    public static <T> T get(String endpoint, Class<T> responseClass, Integer statusCode, ProductQueryParams params,
            String schemaPath) {
        Map<String, Object> queryParam = (params != null) ? mapper.convertValue(params, Map.class) : Map.of();
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .queryParams(queryParam)
                .when()
                .get(endpoint)
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <T> T get(String endpoint, Class<T> responseClass, Integer statusCode, Map<String, Object> params,
            String schemaPath) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .queryParams(params)
                .when()
                .get(endpoint)
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <T> T postNoBody(String endpoint, Class<T> responseClass, Integer statusCode, String schemaPath) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <T, R> T post(String endpoint, Class<T> responseClass, Integer statusCode, R body, String token) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .headers("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <T, R> T post(String endpoint, Class<T> responseClass, Integer statusCode, R body) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <T, R> T put(String endpoint, Class<T> responseClass, Integer statusCode, R body, String token) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .headers("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <T, R> T put(String endpoint, Class<T> responseClass, Integer statusCode, R body) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }

    public static <R> Response put(String endpoint, Integer statusCode, R body) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response();
    }

    public static <T, R> T patch(String endpoint, Class<T> responseClass, Integer statusCode, R body, Object id) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response().as(responseClass);
    }
    public static <T, R> T patch(String endpoint, Class<T> responseClass, Integer statusCode, R body, Object id,String token) {
        return given()
        .baseUri(ConfigLoader.getProperty("baseUrl"))
        .contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + token)
        .body(body)
        .when()
        .patch(endpoint)
        .then()
        .statusCode(statusCode)
        .extract().response().as(responseClass);
    }

    public static <R> Response patch(String endpoint, Integer statusCode, R body, Object id) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response();
    }

    public static Response delete(String endpoint, Object id, String token,Integer statusCode) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .headers("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint + "/" + id)
                .then()
                .statusCode(statusCode)
                .extract().response();
    }

    public static Response delete(String endpoint, Integer statusCode) {
        return given()
                .baseUri(ConfigLoader.getProperty("baseUrl"))
                .when()
                .delete(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response();
    }

}