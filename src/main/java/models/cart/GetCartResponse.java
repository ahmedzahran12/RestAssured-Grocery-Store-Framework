package models.cart;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"items",
"created"
})

public class GetCartResponse {

    @JsonProperty("items")
    public List<Item> items;
    @JsonProperty("created")
    public String created;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
    "productId",
    "id",
    "quantity"
    })
    public static class Item {

        @JsonProperty("productId")
        public Integer productId;
        @JsonProperty("id")
        public Integer id;
        @JsonProperty("quantity")
        public Integer quantity;

    }

}

