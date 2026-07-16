package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {


    @JsonProperty("error")
    private String error;

    public String getErrorMessage(){
        return error;
    }

    public void setErrorMessage(String error){
        this.error = error;
    }
}
