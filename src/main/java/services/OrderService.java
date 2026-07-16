package services;

import com.github.javafaker.Faker;
import endpoints.OrdersEndpoint;
import models.order.CreateOrderRequest;
import models.order.CreateOrderResponse;

public class OrderService {

    public String createOrderAndGetId(String cartId,String token){
        String customerName = new Faker().name().fullName();
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName(customerName).setCartId(cartId);
        CreateOrderResponse response = new OrdersEndpoint().createOrder(201, CreateOrderResponse.class,request,token);
        return response.orderId;
    }
}
