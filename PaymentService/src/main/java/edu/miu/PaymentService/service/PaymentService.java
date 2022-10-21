package edu.miu.PaymentService.service;

import edu.miu.PaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
