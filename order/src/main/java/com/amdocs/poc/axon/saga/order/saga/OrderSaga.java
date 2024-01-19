package com.amdocs.poc.axon.saga.order.saga;

import com.amdocs.poc.axon.saga.core.command.CancelPaymentCommand;
import com.amdocs.poc.axon.saga.core.command.CreatePaymentCommand;
import com.amdocs.poc.axon.saga.core.command.CreateShipmentCommand;
import com.amdocs.poc.axon.saga.core.event.*;
import com.amdocs.poc.axon.saga.core.model.Order;
import com.amdocs.poc.axon.saga.core.model.Payment;
import com.amdocs.poc.axon.saga.core.model.User;
import com.amdocs.poc.axon.saga.core.query.OrderQuery;
import com.amdocs.poc.axon.saga.core.query.PaymentQuery;
import com.amdocs.poc.axon.saga.core.query.UserQuery;
import com.amdocs.poc.axon.saga.order.command.CancelOrderCommand;
import com.amdocs.poc.axon.saga.order.command.PublishOrderCommand;
import com.amdocs.poc.axon.saga.order.command.UpdateOrderCommand;
import com.amdocs.poc.axon.saga.order.event.*;
import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    public OrderSaga() {
        log.debug("OrderSaga.Constructor");
    }

    /** @Autowired
    public OrderSaga(CommandGateway commandGateway, QueryGateway queryGateway) {
        log.debug("OrderSaga.Constructor for axon Gateways");
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    } **/

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent orderCreatedEvent) {
        log.debug("OrderSaga.OrderCreatedEvent for orderId: {}", orderCreatedEvent.getOrderId());
        try {
            UserQuery userQuery = new UserQuery(orderCreatedEvent.getUserId());
            log.debug("OrderSaga.OrderCreatedEvent.queryGateway: {}", queryGateway);
            User user = queryGateway.query(userQuery, ResponseTypes.instanceOf(User.class)).join();
            log.debug("OrderSaga.OrderCreatedEvent.UserQuery for orderId: {}\n\tUser retrieved: {}",
                    orderCreatedEvent.getOrderId(), user);
            CreatePaymentCommand createPaymentCommand = CreatePaymentCommand.builder()
                    .paymentId(UUID.randomUUID().toString())
                    .orderId(orderCreatedEvent.getOrderId())
                    .card(user.getCard())
                    .build();
            commandGateway.sendAndWait(createPaymentCommand);
        } catch (Exception ex) {
            log.error("OrderSaga.OrderCreatedEvent for orderId: {} got Exception: {}",
                    orderCreatedEvent.getOrderId(), ex.getMessage());
            //Compensating actions should follow..
            cancelOrder(orderCreatedEvent.getOrderId());
        }
    }

    @StartSaga
    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderRejectedEvent orderRejectedEvent) {
        log.debug("OrderSaga.OrderRejectedEvent for orderId: {} - END of the saga", orderRejectedEvent.getOrderId());
    }

    private void cancelOrder(String orderId) {
        log.debug("OrderSaga.cancelOrder for orderId: {}", orderId);
        try {
            CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId, OrderStatus.CANCELED);
            log.debug("OrderSaga.cancelOrder.commandGateway: {}", cancelOrderCommand);
            commandGateway.sendAndWait(cancelOrderCommand);
        } catch (Exception ex) {
            log.error("OrderSaga.cancelOrderCommand for orderId: {} got Exception: {}",
                    orderId, ex.getMessage());
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCanceledEvent orderCanceledEvent) {
        log.debug("OrderSaga.OrderCanceledEvent for orderId: {} - END of the saga",
                orderCanceledEvent.getOrderId());
        //SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentCreatedEvent paymentCreatedEvent) {
        log.debug("OrderSaga.PaymentCreatedEvent for paymentId: {}, orderId: {}",
                paymentCreatedEvent.getPaymentId(), paymentCreatedEvent.getOrderId());
        try {
            UpdateOrderCommand updateOrderCommand =
                    new UpdateOrderCommand(paymentCreatedEvent.getOrderId(), OrderStatus.PAID);
            commandGateway.sendAndWait(updateOrderCommand);
        } catch (Exception ex) {
            log.error("OrderSaga.PaymentCreatedEvent for paymentId: {}, orderId: {} got Exception: {}",
                    paymentCreatedEvent.getPaymentId(), paymentCreatedEvent.getOrderId(), ex.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderUpdatedEvent orderUpdatedEvent) {
        log.debug("OrderSaga.OrderUpdatedEvent for orderId: {}", orderUpdatedEvent.getOrderId());
        try {
            if(orderUpdatedEvent.getOrderStatus() == OrderStatus.PAID) {
                CreateShipmentCommand createShipmentCommand = CreateShipmentCommand.builder()
                        .shipmentId(UUID.randomUUID().toString())
                        .orderId(orderUpdatedEvent.getOrderId())
                        .build();
                commandGateway.sendAndWait(createShipmentCommand);
            } else if(orderUpdatedEvent.getOrderStatus() == OrderStatus.SHIPPED) {
                OrderQuery orderQuery = new OrderQuery(orderUpdatedEvent.getOrderId());
                Order order = queryGateway.query(orderQuery, ResponseTypes.instanceOf(Order.class)).join();
                log.debug("OrderSaga.OrderUpdatedEvent.OrderQuery for orderId: {}\n\tOrder retrieved: {}",
                        order.getOrderId(), order);
                PublishOrderCommand publishOrderCommand =
                        new PublishOrderCommand(order.getOrderId(), order.getUserId());
                commandGateway.sendAndWait(publishOrderCommand);
            }
        } catch (Exception ex) {
            log.error("OrderSaga.OrderUpdatedEvent for orderId: {} got Exception: {}",
                    orderUpdatedEvent.getOrderId(), ex.getMessage());
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderPublishedEvent orderPublishedEvent) {
        log.debug("OrderSaga.OrderPublishedEvent for orderId: {} - END of the saga", orderPublishedEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentRejectedEvent paymentRejectedEvent) {
        log.debug("OrderSaga.PaymentRejectedEvent for paymentId: {}, orderId: {}",
                paymentRejectedEvent.getPaymentId(), paymentRejectedEvent.getOrderId());
        cancelOrder(paymentRejectedEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(ShipmentCreatedEvent shipmentCreatedEvent) {
        log.debug("OrderSaga.ShipmentCreatedEvent for shipmentId: {}, orderId: {}",
                shipmentCreatedEvent.getShipmentId(), shipmentCreatedEvent.getOrderId());
        try {
            UpdateOrderCommand updateOrderCommand =
                    new UpdateOrderCommand(shipmentCreatedEvent.getOrderId(), OrderStatus.SHIPPED);
            commandGateway.sendAndWait(updateOrderCommand);

        } catch (Exception ex) {
            log.error("OrderSaga.ShipmentCreatedEvent for shipmentId: {}, orderId: {} got Exception: {}",
                    shipmentCreatedEvent.getShipmentId(), shipmentCreatedEvent.getOrderId(), ex.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(ShipmentRejectedEvent shipmentRejectedEvent) {
        log.debug("OrderSaga.ShipmentRejectedEvent for shipmentId: {}, orderId: {}",
                shipmentRejectedEvent.getShipmentId(), shipmentRejectedEvent.getOrderId());
        cancelPaymentForOrder(shipmentRejectedEvent.getOrderId());
    }

    /**
     * Note. One could also Get payment for orderId from inside of payment service
     * rather than using external Query in the core (approach depends on use case )
     **/
    private void cancelPaymentForOrder(String orderId) {
        log.debug("OrderSaga.cancelPaymentForOrder for orderId: {}", orderId);
        try {
            PaymentQuery paymentQuery = new PaymentQuery(orderId);
            Payment payment = queryGateway.query(paymentQuery, ResponseTypes.instanceOf(Payment.class)).join();
            log.debug("OrderSaga.cancelPaymentForOrder.PaymentQuery for orderId: {}\n\tPayment retrieved: {}",
                    payment.getOrderId(), payment);
            CancelPaymentCommand cancelPaymentCommand =
                    new CancelPaymentCommand(payment.getPaymentId(), payment.getOrderId());
            commandGateway.sendAndWait(cancelPaymentCommand);
        } catch (Exception ex) {
            log.error("OrderSaga.cancelPaymentForOrder for orderId: {} got Exception: {}", orderId, ex.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentCanceledEvent paymentCanceledEvent) {
        log.debug("OrderSaga.PaymentCanceledEvent for orderId: {}", paymentCanceledEvent.getOrderId());
        cancelOrder(paymentCanceledEvent.getOrderId());
    }

}
