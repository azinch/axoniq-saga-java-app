package com.amdocs.poc.axon.saga.payment.event;

import com.amdocs.poc.axon.saga.core.event.PaymentCanceledEvent;
import com.amdocs.poc.axon.saga.core.event.PaymentCreatedEvent;
import com.amdocs.poc.axon.saga.core.event.PaymentRejectedEvent;
import com.amdocs.poc.axon.saga.core.model.PaymentStatus;
import com.amdocs.poc.axon.saga.payment.data.PaymentEntity;
import com.amdocs.poc.axon.saga.payment.data.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class PaymentEventHandler {
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentCreatedEvent paymentCreatedEvent) {
        log.debug("PaymentEventHandler.PaymentCreatedEvent for paymentId: {}, orderId: {}",
                paymentCreatedEvent.getPaymentId(), paymentCreatedEvent.getOrderId());
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentId(paymentCreatedEvent.getPaymentId())
                .orderId(paymentCreatedEvent.getOrderId())
                .timeStamp(new Date())
                .paymentStatus(PaymentStatus.CREATED)
                .build();
        paymentRepository.save(paymentEntity);
    }

    @EventHandler
    public void on(PaymentRejectedEvent paymentRejectedEvent) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentId(paymentRejectedEvent.getPaymentId())
                .orderId(paymentRejectedEvent.getOrderId())
                .timeStamp(new Date())
                .paymentStatus(PaymentStatus.REJECTED)
                .build();
        paymentRepository.save(paymentEntity);
    }

    @EventHandler
    public void on(PaymentCanceledEvent paymentCanceledEvent) {
        PaymentEntity paymentEntity = paymentRepository.getById(paymentCanceledEvent.getPaymentId());
        paymentEntity.setPaymentStatus(PaymentStatus.CANCELED);
        paymentRepository.save(paymentEntity);
    }
}
