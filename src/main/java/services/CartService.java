package services;

import endpoints.CartEndpoint;
import io.restassured.response.Response;
import models.cart.*;
import models.product.ProductResponse;

public class CartService {

    public static String createNewCartAndGetId(){
        CreateCartResponse response;
        response = new CartEndpoint().createNewCart(201,"schemas/cart-created-schema.json");
        return response.cartId;
    }
    public static Response getCart(String cartId){
        return new CartEndpoint().getCart(200,"schemas/cart-schema.json", Response.class,cartId);
    }
    public static CartItemResponse AddProductAndGetItem(String cartId, Integer productId, Integer quantity){
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId).setQuantity(quantity);
        return new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class, cartId);
    }
    public static CartItem addItemToCart(String cartId, Integer productId, Integer quantity){
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId).setQuantity(quantity);
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cartId).setQuantity(quantity).setProductId(productId);
        CartItemResponse response = new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class, cartId);
        cartItem.setItemId(response.itemId);
        return cartItem;
    }
    public static CartItem addRandomItemToCart(String cartId){
        Integer productId = ProductService.getRandomAvailableProduct().id;
        ProductResponse product = ProductService.getProductById(productId);
        Integer quantity = ProductService.getRandomQuantity(product);
        return addItemToCart(cartId,productId,quantity);
    }
}
