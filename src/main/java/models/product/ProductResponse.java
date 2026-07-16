package models.product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"category",
"name",
"manufacturer",
"price",
"current-stock",
"inStock"
})

public class ProductResponse {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("category")
    public String category;
    @JsonProperty("name")
    public String name;
    @JsonProperty("manufacturer")
    public String manufacturer;
    @JsonProperty("price")
    public Float price;
    @JsonProperty("current-stock")
    public Integer currentStock;
    @JsonProperty("inStock")
    public Boolean inStock;

}