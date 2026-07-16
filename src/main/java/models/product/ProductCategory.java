package models.product;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductCategory {
    MEAT_SEAFOOD("meat-seafood"),
    FRESH_PRODUCE("fresh-produce"),
    CANDY("candy"),
    BREAD_BAKERY("bread-bakery"),
    DAIRY("dairy"),
    EGGS("eggs"),
    COFFEE("coffee");

    private final String value;
    ProductCategory(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}
