package edu.miu.OrderService.service;

import edu.miu.OrderService.entity.Order;
import edu.miu.OrderService.model.OrderRequest;
import edu.miu.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public long placeOrder(OrderRequest orderRequest) {

        //Order entity -> save the data with status order created
        // Product service -> Block Product(Reduce the quantity)
        //payment service -> payment -> success -> COMPLETE else CANCELLED


        log.info("placing order request: {}", orderRequest);

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Order Placed successfully with order Id:{} ", order.getId());
        return order.getId();
    }
}
