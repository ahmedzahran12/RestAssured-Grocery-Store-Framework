package services;


import com.github.javafaker.Faker;
import endpoints.ClientEndpoint;
import models.client.ClientRegisterRequest;
import models.client.ClientRegisterResponse;
import utils.ConfigLoader;

public class TokenService {
    private static final ThreadLocal<String> cachedToken = new ThreadLocal<>();
    public static String getToken(){
        if(cachedToken.get() == null || cachedToken.get().isEmpty()) {
            Faker faker = new Faker();

            String clientName = ConfigLoader.getProperty("clientName");
            String clientEmail =  ConfigLoader.getProperty("clientEmail");

            if(clientName == null || clientName.isEmpty()){
                clientName = faker.name().fullName();
            }
            if(clientEmail == null || clientEmail.isEmpty()){
                clientEmail = faker.internet().emailAddress();
            }
            ClientRegisterRequest request = new ClientRegisterRequest();
            request.setClientName(clientName).setClientEmail(clientEmail);
            ClientRegisterResponse response ;
            response = new ClientEndpoint().registerClient(201, ClientRegisterResponse.class, request, "schemas/client-created-schema.json");
            cachedToken.set(response.accessToken);

       }
        return cachedToken.get();
    }
}
