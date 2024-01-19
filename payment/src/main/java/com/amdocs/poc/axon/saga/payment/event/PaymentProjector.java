package com.amdocs.poc.axon.saga.payment.event;

import com.amdocs.poc.axon.saga.core.model.Payment;
import com.amdocs.poc.axon.saga.core.query.PaymentQuery;
import com.amdocs.poc.axon.saga.payment.data.PaymentEntity;
import com.amdocs.poc.axon.saga.payment.data.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentProjector {
    private PaymentRepository paymentRepository;

    public PaymentProjector() {
        log.debug("PaymentRepository.constructor");
    }

    @Autowired
    public PaymentProjector(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @QueryHandler
    public Payment getPayment(PaymentQuery paymentQuery) {
        log.debug("PaymentProjector.getPayment");
        PaymentEntity paymentEntity = paymentRepository.findByOrderIdNative(paymentQuery.getOrderId());
        Payment payment = Payment.builder()
                .paymentId(paymentEntity.getPaymentId())
                .orderId(paymentEntity.getOrderId())
                .paymentStatus(paymentEntity.getPaymentStatus())
                .build();
        log.debug("PaymentProjector.getPayment for query: {}\n\tPaymentEntity retrieved: {}\n\tPayment retrieved: {}",
                paymentQuery, paymentEntity, payment);
        return payment;
    }

}


