package models.cart;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"productId",
"quantity"
})
public class CartItemRequest {

    @JsonProperty("productId")
    private Integer productId;
    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("productId")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public CartItemRequest setProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public CartItemRequest setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

}