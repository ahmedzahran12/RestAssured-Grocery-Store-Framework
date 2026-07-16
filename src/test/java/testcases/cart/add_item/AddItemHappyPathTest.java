package testcases.cart.add_item;

import endpoints.CartEndpoint;
import endpoints.ProductEndpoint;
import models.cart.CartItemRequest;
import models.cart.CartItemResponse;
import models.cart.GetCartResponse;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;

public class AddItemHappyPathTest {

    @Test
    public void testAddingItemAndVerifyItExists() {
        // Arrange
        Integer myProductId = ProductService.getRandomAvailableProduct().id;
        String myCartId = CartService.createNewCartAndGetId();
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(myProductId).setQuantity(2);
        CartItemResponse cartItemResponse;
        GetCartResponse.Item[] items;
        // Act
        cartItemResponse = new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class, myCartId);
        items = new CartEndpoint().getCartItems(200, "schemas/cart-items-schema.json", GetCartResponse.Item[].class,
                myCartId);
        // Assert
        Assert.assertTrue(cartItemResponse.created);
        Assert.assertNotNull(cartItemResponse.itemId);
        Assert.assertEquals(items.length, 1, "Expected at least 1 item in cart");
        Assert.assertEquals(items[0].productId, myProductId, "Product Id mismatch");
        Assert.assertEquals(items[0].quantity, 2, "Quantity mismatch");
        Assert.assertEquals(items[0].id, cartItemResponse.itemId, "Item Id mismatch");
    }

    @Test
    public void testAddingMultipleItemsToCartNoDuplicates() {
        // Arrange
        String myCartId = CartService.createNewCartAndGetId();
        CartItemResponse cartItemResponse;
        GetCartResponse.Item[] items;
        CartItemRequest cartItemRequest = new CartItemRequest();
        ProductResponse[] products = ProductService.getAllProducts();
        int addedCount = 0;
        for (int i = 0; i < products.length; i++) {
            if (products[i].inStock != null && products[i].inStock) {
                // Act
                cartItemRequest.setProductId(products[i].id);
                cartItemResponse = new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class,
                        myCartId);
                items = new CartEndpoint().getCartItems(200, "schemas/cart-items-schema.json",
                        GetCartResponse.Item[].class,
                        myCartId);
                addedCount++;
                // Assert
                Assert.assertTrue(cartItemResponse.created);
                Assert.assertEquals(items.length, addedCount, "Expected " + addedCount + " items in cart");
                Assert.assertEquals(items[addedCount - 1].productId, products[i].id, "Product Id mismatch");
            }
        }
    }

    @Test
    public void testAddingItemWithQuantitySameAsStock() {
        // Arrange
        String myCartId = CartService.createNewCartAndGetId();
        ProductResponse productResponse = ProductService.getRandomAvailableProduct();
        Integer productId = productResponse.id;
        Integer stock = ProductService.getProductById(productId).currentStock;
        CartItemRequest cartItemRequest = new CartItemRequest();
        CartItemResponse cartItemResponse;
        GetCartResponse.Item[] items;
        // Act
        cartItemRequest.setProductId(productId).setQuantity(stock);
        cartItemResponse = new CartEndpoint().AddCartItem(201, cartItemRequest, CartItemResponse.class, myCartId);
        items = new CartEndpoint().getCartItems(200, "schemas/cart-items-schema.json", GetCartResponse.Item[].class,
                myCartId);
        // Assert
        Assert.assertTrue(cartItemResponse.created);
        Assert.assertEquals(items[0].productId, productId, "Product Id mismatch");
        Assert.assertEquals(items[0].quantity, stock, "Quantity mismatch");
    }

}
