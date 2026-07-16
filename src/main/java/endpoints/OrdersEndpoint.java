package endpoints;

import io.restassured.response.Response;
import models.order.CreateOrderRequest;
import utils.RestHelper;

public class OrdersEndpoint {
    private final String orderEndpoint = "/orders";

    public <T> T getOrderById(Integer statusCode,String schemaPath,Class<T> responseClass, String orderId,String token){
        return RestHelper.get(orderEndpoint + "/" + orderId,responseClass,statusCode,schemaPath,token);
    }
    public <T> T getAllOrders(Integer statusCode,String schemaPath,Class<T> responseClass,String token){
        return RestHelper.get(orderEndpoint,responseClass,statusCode,schemaPath,token);
    }
    public <T> T createOrder(Integer statusCode, Class<T> responseClass, CreateOrderRequest request,String token){
        return RestHelper.post(orderEndpoint,responseClass,statusCode,request,token);
    }
    public <T> T updateOrder(Integer statusCode, Class<T> responseClass, CreateOrderRequest request,String token,String orderId){
        return RestHelper.patch(orderEndpoint + "/" + orderId,responseClass,statusCode,request,token);
    }
    public <T> Response deleteOrder(Integer statusCode, String orderId, String token){
        return RestHelper.delete(orderEndpoint + "/" + orderId,orderId,token,statusCode);
    }
}
