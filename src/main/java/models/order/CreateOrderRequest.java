package models.order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"cartId",
"customerName",
"comment"
})

public class CreateOrderRequest {

    @JsonProperty("cartId")
    private String cartId;
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("comment")
    private String comment;

    @JsonProperty("cartId")
    public String getCartId() {
        return cartId;
    }

    @JsonProperty("cartId")
    public CreateOrderRequest setCartId(String cartId) {
        this.cartId = cartId;
        return this;
    }

    @JsonProperty("customerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("customerName")
    public CreateOrderRequest setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public CreateOrderRequest setComment(String comment) {
        this.comment = comment;
        return this;
    }

}