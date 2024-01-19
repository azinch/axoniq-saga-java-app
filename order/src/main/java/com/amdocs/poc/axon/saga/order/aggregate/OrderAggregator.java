package com.amdocs.poc.axon.saga.order.aggregate;

import com.amdocs.poc.axon.saga.core.event.OrderPublishedEvent;
import com.amdocs.poc.axon.saga.order.command.CancelOrderCommand;
import com.amdocs.poc.axon.saga.order.command.CreateOrderCommand;
import com.amdocs.poc.axon.saga.order.command.PublishOrderCommand;
import com.amdocs.poc.axon.saga.order.command.UpdateOrderCommand;
import com.amdocs.poc.axon.saga.order.event.*;
import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import java.util.Random;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class OrderAggregator {
    @AggregateIdentifier
    private String orderId;
    private String userId;
    private String addressId;
    private String productId;
    private Integer quantity;
    private OrderStatus orderStatus;

    public OrderAggregator() {
    }

    @CommandHandler
    public OrderAggregator(CreateOrderCommand createOrderCommand) {
        //Validations and rest of the Order business logic should be put here..
        int intRandom = new Random().nextInt(100);
        log.debug("OrderAggregator.CreateOrderCommand for orderId: {} with intRandom: {}",
                createOrderCommand.getOrderId(), intRandom);
        if((intRandom % 2) != 0) {
            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
            BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
            apply(orderCreatedEvent);
        } else {
            OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent();
            BeanUtils.copyProperties(createOrderCommand, orderRejectedEvent);
            //!! Re-setting the order to right status
            orderRejectedEvent.setOrderStatus(OrderStatus.REJECTED);
            apply(orderRejectedEvent);
        }
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrderId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.productId = orderCreatedEvent.getProductId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
        log.debug("EventSourcingHandler.OrderCreatedEvent with orderId: {}", orderId);
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        this.orderId = orderRejectedEvent.getOrderId();
        this.userId = orderRejectedEvent.getUserId();
        this.addressId = orderRejectedEvent.getAddressId();
        this.productId = orderRejectedEvent.getProductId();
        this.quantity = orderRejectedEvent.getQuantity();
        this.orderStatus = orderRejectedEvent.getOrderStatus();
        log.debug("EventSourcingHandler.OrderRejectedEvent with orderId: {}", orderId);
    }

    @CommandHandler
    public void handle(CancelOrderCommand cancelOrderCommand) {
        log.debug("OrderAggregator.CancelOrderCommand for orderId: {}", cancelOrderCommand.getOrderId());
        //Validations and rest of the Order business logic should be put here..
        OrderCanceledEvent orderCanceledEvent =
                new OrderCanceledEvent(cancelOrderCommand.getOrderId(), cancelOrderCommand.getOrderStatus());
        apply(orderCanceledEvent);
    }

    @EventSourcingHandler
    public void on(OrderCanceledEvent orderCanceledEvent) {
        this.orderStatus = orderCanceledEvent.getOrderStatus();
        log.debug("EventSourcingHandler.OrderCanceledEvent with orderId: {}", orderId);
    }

    @CommandHandler
    public void handle(UpdateOrderCommand updateOrderCommand) {
        log.debug("OrderAggregator.UpdateOrderCommand for orderId: {}", updateOrderCommand.getOrderId());
        OrderUpdatedEvent orderUpdatedEvent =
                new OrderUpdatedEvent(updateOrderCommand.getOrderId(), updateOrderCommand.getOrderStatus());
        apply(orderUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderUpdatedEvent orderUpdatedEvent) {
        this.orderStatus = orderUpdatedEvent.getOrderStatus();
        log.debug("EventSourcingHandler.OrderUpdatedEvent with orderId: {}", orderId);
    }

    @CommandHandler
    public void handle(PublishOrderCommand publishOrderCommand) {
        log.debug("OrderAggregator.PublishOrderCommand for orderId: {}", publishOrderCommand.getOrderId());
        OrderPublishedEvent orderPublishedEvent = new OrderPublishedEvent();
        BeanUtils.copyProperties(publishOrderCommand, orderPublishedEvent);
        apply(orderPublishedEvent);
    }

    @EventSourcingHandler
    public void on(OrderPublishedEvent orderPublishedEvent) {
        this.orderStatus = OrderStatus.PUBLISHED;
        log.debug("EventSourcingHandler.OrderPublishedEvent with orderId: {}", orderId);
    }

}
