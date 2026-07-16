package models.cart;

public class CartItem {
    private Integer itemId;
    private Integer productId;
    private Integer quantity;
    private String cartId;

    public Integer getItemId() {
        return itemId;
    }

    public CartItem setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public Integer getProductId() {
        return productId;
    }

    public CartItem setProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getCartId() {
        return cartId;
    }

    public CartItem setCartId(String cartId) {
        this.cartId = cartId;
        return this;
    }
}
