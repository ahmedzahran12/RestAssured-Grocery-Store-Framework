package models.order;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"items",
"customerName",
"created",
"comment"
})

public class GetOrder {

    @JsonProperty("id")
    public String id;
    @JsonProperty("items")
    public List<Item> items;
    @JsonProperty("customerName")
    public String customerName;
    @JsonProperty("created")
    public String created;
    @JsonProperty("comment")
    public String comment;

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