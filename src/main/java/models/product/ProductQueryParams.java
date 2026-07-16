package models.product;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductQueryParams {
    private  ProductCategory category;
    private  Integer results;
    private  Boolean available;

    public void setCategory(ProductCategory category){
        this.category = category;
    }
    public void setResults(Integer results){
        this.results = results;
    }
    public void setAvailable(Boolean available){
        this.available = available;
    }
    public ProductCategory getCategory(){
        return category;
    }
    public Integer getResults(){
        return results;
    }
    public Boolean getAvailable(){
        return available;
    }
}
