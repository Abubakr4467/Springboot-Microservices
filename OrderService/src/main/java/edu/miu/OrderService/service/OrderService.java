package edu.miu.OrderService.service;


import edu.miu.OrderService.model.OrderRequest;
import edu.miu.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
