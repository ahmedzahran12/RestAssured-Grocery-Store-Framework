

package models.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"created",
"cartId"
})
public class CreateCartResponse {

    @JsonProperty("created")
    public Boolean created;
    @JsonProperty("cartId")
    public String cartId;

}