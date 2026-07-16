package testcases.auth;

import com.github.javafaker.Faker;
import endpoints.ClientEndpoint;
import models.ErrorResponse;
import models.client.ClientRegisterRequest;
import models.client.ClientRegisterResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
public class RegistrationTest {
    @Test
    public void testValidSuccessfulRegistration(){
        //Arrange
        ClientRegisterRequest request = new ClientRegisterRequest();
        String clientName = new Faker().name().fullName();
        String email = new Faker().internet().emailAddress();
        request.setClientName(clientName).setClientEmail(email);
        //Act
        ClientRegisterResponse response = new ClientEndpoint().registerClient(201, ClientRegisterResponse.class, request, "schemas/client-created-schema.json");
        //Assert
        Assert.assertNotNull(response.accessToken, "Expected non-null access token");
        Assert.assertNotEquals(response.accessToken.trim(), "", "Expected non-empty access token");
    }
    @Test
    public void testRegistrationWithMissingEmail(){
        //Arrange
        ClientRegisterRequest request = new ClientRegisterRequest();
        String name = new Faker().name().fullName();
        request.setClientName(name);
        ErrorResponse response;
        //Act
        response = new ClientEndpoint().registerClient(400, ErrorResponse.class, request, "schemas/error-schema.json");
        //Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorMessage(),"Invalid or missing client email.");
    }
    @Test
    public void testRegistrationWithMissingClientName(){
        ClientRegisterRequest request = new ClientRegisterRequest();
        String email = new Faker().internet().emailAddress();
        request.setClientEmail(email);
        ErrorResponse response;
        //Act
        response = new ClientEndpoint().registerClient(400, ErrorResponse.class, request, "schemas/error-schema.json");
        //Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorMessage(),"Invalid or missing client name.");
    }
    @Test
    public void testRegistrationWithInvalidEmailFormat(){
        ClientRegisterRequest request = new ClientRegisterRequest();
        String invalidEmail = new Faker().name().fullName();
        String name = new Faker().name().fullName();
        request.setClientName(name).setClientEmail(invalidEmail);
        ErrorResponse response;
        //Act
        response = new ClientEndpoint().registerClient(400, ErrorResponse.class, request, "schemas/error-schema.json");
        //Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorMessage(),"Invalid or missing client email.");
    }
    @Test
    public void testRegistrationWithEmptyClientName(){
        ClientRegisterRequest request = new ClientRegisterRequest();
        String email = new Faker().internet().emailAddress();
        request.setClientName("").setClientEmail(email);
        ErrorResponse response;
        //Act
        response = new ClientEndpoint().registerClient(400, ErrorResponse.class, request, "schemas/error-schema.json");
        //Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorMessage(),"Invalid or missing client name.");
    }
    @Test
    public void testRegistrationWithEmptyClientEmail(){
        ClientRegisterRequest request = new ClientRegisterRequest();
        String name = new Faker().name().fullName();
        request.setClientName(name).setClientEmail("");
        ErrorResponse response;
        //Act
        response = new ClientEndpoint().registerClient(400, ErrorResponse.class, request, "schemas/error-schema.json");
        //Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorMessage(),"Invalid or missing client email.");
    }
    @Test
    public void testRegistrationWithEmailAlreadyRegistered(){
        //Arrange
        ClientRegisterRequest request = new ClientRegisterRequest();
        String email = new Faker().internet().emailAddress();
        String clientName = new Faker().name().fullName();
        request.setClientEmail(email).setClientName(clientName);
        // First registration should succeed (Act)
        ClientRegisterResponse firstResponse = new ClientEndpoint().registerClient(201, ClientRegisterResponse.class, request, "schemas/client-created-schema.json");
        Assert.assertNotNull(firstResponse.accessToken, "Expected non-null access token for first registration");
        // Second registration with the same email should fail
        ErrorResponse secondResponse = new ClientEndpoint().registerClient(409, ErrorResponse.class, request, "schemas/error-schema.json");
        //Assert
        Assert.assertNotNull(secondResponse);
        Assert.assertEquals(secondResponse.getErrorMessage(),"API client already registered. Try a different email.");

    }
}
