package endpoints;

import io.restassured.response.Response;
import models.cart.CartItemRequest;
import models.cart.CreateCartResponse;
import utils.RestHelper;

public class CartEndpoint {
    private final String cartEndpoint = "/carts";

    public <T> T getCart(Integer statusCode, String schemaPath, Class<T> responseClass, String CartId) {
        return RestHelper.get(cartEndpoint, CartId, responseClass, statusCode, schemaPath);
    }

    public CreateCartResponse createNewCart(Integer statusCode, String schemaPath) {
        return RestHelper.postNoBody(cartEndpoint, CreateCartResponse.class, statusCode, schemaPath);
    }

    public <T> T AddCartItem(Integer statusCode, CartItemRequest request, Class<T> responseClass, String CartId) {
        return RestHelper.post(cartEndpoint + "/" + CartId + "/items", responseClass, statusCode, request, CartId);
    }

    public <T> T getCartItems(Integer statusCode, String schemaPath, Class<T> responseClass, String CartId) {
        return RestHelper.get(cartEndpoint + "/" + CartId + "/items", responseClass, statusCode, schemaPath);
    }

    public <T> T modifyItemInCart(Integer statusCode, CartItemRequest request, Class<T> responseClass, String CartId,
            Integer itemId) {
        return RestHelper.patch(cartEndpoint + "/" + CartId + "/items/" + itemId, responseClass, statusCode, request,
                CartId);
    }

    public Response modifyItemInCart(Integer statusCode, CartItemRequest request, String CartId, Integer itemId) {
        return RestHelper.patch(cartEndpoint + "/" + CartId + "/items/" + itemId, statusCode, request, CartId);
    }

    public <T> T replaceItemInCart(Integer statusCode, CartItemRequest request, Class<T> responseClass, String CartId,
            Integer itemId) {
        return RestHelper.put(cartEndpoint + "/" + CartId + "/items/" + itemId, responseClass, statusCode, request);
    }

    public Response replaceItemInCart(Integer statusCode, CartItemRequest request, String CartId, Integer itemId) {
        return RestHelper.put(cartEndpoint + "/" + CartId + "/items/" + itemId, statusCode, request);
    }

    public Response deleteItemInCart( String cartId, Integer itemId,Integer statusCode) {
        return RestHelper.delete(cartEndpoint + "/" + cartId + "/items/" + itemId,statusCode);
    }
}
