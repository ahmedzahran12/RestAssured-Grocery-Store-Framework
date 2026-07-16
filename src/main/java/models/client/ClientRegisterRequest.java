package models.client;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"clientName",
"clientEmail"
})

public class ClientRegisterRequest {

    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("clientEmail")
    private String clientEmail;

    @JsonProperty("clientName")
    public String getClientName() {
        return clientName;
    }

    @JsonProperty("clientName")
    public ClientRegisterRequest setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    @JsonProperty("clientEmail")
    public String getClientEmail() {
        return clientEmail;
    }

    @JsonProperty("clientEmail")
    public ClientRegisterRequest setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
        return this;
    }

}