package edu.miu.OrderService.service;

import edu.miu.OrderService.entity.Order;
import edu.miu.OrderService.exception.CustomException;
import edu.miu.OrderService.external.client.PaymentService;
import edu.miu.OrderService.external.client.ProductService;
import edu.miu.OrderService.external.request.PaymentRequest;
import edu.miu.OrderService.model.OrderRequest;
import edu.miu.OrderService.model.OrderResponse;
import edu.miu.OrderService.repository.OrderRepository;
import edu.miu.ProductService.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public long placeOrder(OrderRequest orderRequest) {

        //Order entity -> save the data with status order created
        // Product service -> Block Product(Reduce the quantity)
        //payment service -> payment -> success -> COMPLETE else CANCELLED


        log.info("placing order request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        log.info("Creating order with status CREATED");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling Payment Service to complete the payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);

            log.info("Payment done successfully. Changing the Order status to PLACED");

            orderStatus ="PLACED";

        }catch (Exception e ){
            log.error("Error occured in payment. Changing order status to PAYMENT_FAILED");
            orderStatus= "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed successfully with order Id:{} ", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id: {} ", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow( ()-> new CustomException("Order not found for Order Id : " + orderId, "NOT_FOUND", 404));


        log.info("Invoking product service to fetch the product for id : {} ",order.getProductId());


        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class );

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();


        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .build();
        return orderResponse;
    }
}
