package testcases.order.create_order;

import com.github.javafaker.Faker;
import endpoints.OrdersEndpoint;
import models.cart.CartItem;
import models.cart.CartItemResponse;
import models.order.CreateOrderRequest;
import models.order.CreateOrderResponse;
import models.order.GetOrder;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.CartService;
import services.TokenService;

public class CreateOrderTest {

    @Test
    public void testCreateOrderAndValidateSuccess(){
        //Arrange
        String cartId = CartService.createNewCartAndGetId();
        CartItem item = CartService.addRandomItemToCart(cartId);
        CreateOrderRequest request = new CreateOrderRequest();
        String customerName = new Faker().name().fullName();
        request.setCartId(cartId).setCustomerName(customerName);
        String token = TokenService.getToken();
        CreateOrderResponse createOrder;
        GetOrder getOrder;
        //Act
        createOrder = new OrdersEndpoint().createOrder(201,CreateOrderResponse.class,request,token);
        String orderId = createOrder.orderId;
        getOrder = new OrdersEndpoint().getOrderById(200,"schemas/order-schema.json",GetOrder.class,orderId,token);
        //Assert
        Assert.assertTrue(createOrder.created,"Created should be true");
        Assert.assertEquals(getOrder.customerName,customerName,"Customer Name is incorrect");
        Assert.assertEquals(getOrder.id,orderId,"Order ID is incorrect");
        Assert.assertNotNull(getOrder,"Order is null");
        Assert.assertNotNull(getOrder.items,"items added should exist in order");
        Assert.assertEquals(getOrder.items.get(0).id,item.getItemId(),"item id in order should match item added");
        Assert.assertEquals(getOrder.items.get(0).productId,item.getProductId(),"product id in order should match item added");
        Assert.assertEquals(getOrder.items.get(0).quantity,item.getQuantity(),"quantity in order should match quantity of item added");
    }


}
