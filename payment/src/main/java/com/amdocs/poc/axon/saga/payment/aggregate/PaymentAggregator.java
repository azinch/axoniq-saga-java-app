package com.amdocs.poc.axon.saga.payment.aggregate;

import com.amdocs.poc.axon.saga.core.command.CancelPaymentCommand;
import com.amdocs.poc.axon.saga.core.command.CreatePaymentCommand;
import com.amdocs.poc.axon.saga.core.event.PaymentCanceledEvent;
import com.amdocs.poc.axon.saga.core.event.PaymentCreatedEvent;
import com.amdocs.poc.axon.saga.core.event.PaymentRejectedEvent;
import com.amdocs.poc.axon.saga.core.model.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import java.util.Random;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class PaymentAggregator {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private PaymentStatus paymentStatus;

    public PaymentAggregator() {
    }

    @CommandHandler
    public PaymentAggregator(CreatePaymentCommand createPaymentCommand) {
        int intRandom = new Random().nextInt(100);
        log.debug("PaymentAggregator.CreatePaymentCommand for paymentId: {}, orderId: {} with intRandom: {}",
                createPaymentCommand.getPaymentId(), createPaymentCommand.getOrderId(), intRandom);
        //Validations and rest of the Payment business logic should be put here..
        if((intRandom % 2) != 0) {
            PaymentCreatedEvent paymentCreatedEvent =
                    new PaymentCreatedEvent(createPaymentCommand.getPaymentId(), createPaymentCommand.getOrderId());
            apply(paymentCreatedEvent);
        } else {
            PaymentRejectedEvent paymentRejectedEvent =
                    new PaymentRejectedEvent(createPaymentCommand.getPaymentId(), createPaymentCommand.getOrderId());
            apply(paymentRejectedEvent);
        }
    }

    @EventSourcingHandler
    public void on(PaymentCreatedEvent paymentCreatedEvent) {
        this.paymentId = paymentCreatedEvent.getPaymentId();
        this.orderId = paymentCreatedEvent.getOrderId();
        this.paymentStatus = PaymentStatus.CREATED;
        log.debug("EventSourcingHandler.PaymentCreatedEvent with paymentId: {}, orderId: {}", paymentId, orderId);
    }

    @EventSourcingHandler
    public void on(PaymentRejectedEvent paymentRejectedEvent) {
        this.paymentId = paymentRejectedEvent.getPaymentId();
        this.orderId = paymentRejectedEvent.getOrderId();
        this.paymentStatus = PaymentStatus.REJECTED;
        log.debug("EventSourcingHandler.PaymentRejectedEvent with paymentId: {}, orderId: {}", paymentId, orderId);
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) {
        log.debug("PaymentAggregator.CancelPaymentCommand for orderId: {}", cancelPaymentCommand.getOrderId());
        PaymentCanceledEvent paymentCanceledEvent =
                new PaymentCanceledEvent(cancelPaymentCommand.getPaymentId(), cancelPaymentCommand.getOrderId());
        apply(paymentCanceledEvent);
    }

    @EventSourcingHandler
    public void on(PaymentCanceledEvent paymentCanceledEvent) {
        this.paymentStatus = PaymentStatus.CANCELED;
        log.debug("EventSourcingHandler.paymentCanceledEvent with paymentId: {}, orderId: {}", paymentId, orderId);
    }

}
