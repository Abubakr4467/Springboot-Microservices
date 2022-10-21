package edu.miu.PaymentService.service;

import edu.miu.PaymentService.entity.TransactionDetails;
import edu.miu.PaymentService.model.PaymentRequest;
import edu.miu.PaymentService.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImp implements  PaymentService {
    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;


    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording Payment Details: {}", paymentRequest);
        //create entity that is  transaction details  from payment request we got  save data to database

        TransactionDetails transactionDetails
                = TransactionDetails.builder()
                .paymentDate(Instant.now() )
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        transactionDetailsRepository.save(transactionDetails);

        log.info("Transaction Comleted with Id: {}" , transactionDetails.getId());

        return transactionDetails.getId();
    }
}
