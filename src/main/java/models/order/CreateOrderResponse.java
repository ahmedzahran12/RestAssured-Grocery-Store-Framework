package models.order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"created",
"orderId"
})
public class CreateOrderResponse {

    @JsonProperty("created")
    public Boolean created;
    @JsonProperty("orderId")
    public String orderId;

}