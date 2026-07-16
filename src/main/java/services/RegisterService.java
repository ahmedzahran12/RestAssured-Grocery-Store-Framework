package services;

import com.github.javafaker.Faker;
import endpoints.ClientEndpoint;
import models.client.ClientRegisterRequest;
import models.client.ClientRegisterResponse;

public class RegisterService {
    public static String registerClientAndGetToken(ClientRegisterRequest clientData)
    {
        ClientRegisterResponse response = new ClientEndpoint().registerClient(201,ClientRegisterResponse.class,clientData,"schemas/client-created-schema.json");
        return response.accessToken;
    }
    public static String registerClientAndGetToken(){
        ClientRegisterRequest request = new ClientRegisterRequest();
        request.setClientName(Faker.instance().name().fullName());
        request.setClientEmail(Faker.instance().internet().emailAddress());
        ClientRegisterResponse response = new ClientEndpoint().registerClient(201,ClientRegisterResponse.class,request,"schemas/client-created-schema.json");
        return response.accessToken;
    }
}
