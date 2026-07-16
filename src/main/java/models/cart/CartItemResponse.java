
package models.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"created",
"itemId"
})

public class CartItemResponse {

    @JsonProperty("created")
    public Boolean created;
    @JsonProperty("itemId")
    public Integer itemId;

}