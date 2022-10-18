package edu.miu.OrderService.service;


import edu.miu.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
