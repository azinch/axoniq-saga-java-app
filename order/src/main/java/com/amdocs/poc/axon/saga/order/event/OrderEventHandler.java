package com.amdocs.poc.axon.saga.order.event;

import com.amdocs.poc.axon.saga.core.event.OrderPublishedEvent;
import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import com.amdocs.poc.axon.saga.order.data.OrderEntity;
import com.amdocs.poc.axon.saga.order.data.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
//@ProcessingGroup(value = "order-handling")
@Slf4j
public class OrderEventHandler {
    @Value("${kafkaTopic: axon-saga}")
    private String kafkaTopic;

    private OrderRepository orderRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public OrderEventHandler(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        objectMapper = new ObjectMapper();
        log.debug("OrderEventHandler constructor for orderRepository: {}, kafkaTemplate: {}",
                orderRepository, kafkaTemplate);
    }

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        log.debug("OrderEventHandler.OrderCreatedEvent for orderId: {}", orderCreatedEvent.getOrderId());
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent, orderEntity);
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        log.debug("OrderEventHandler.OrderRejectedEvent for orderId: {}", orderRejectedEvent.getOrderId());
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderRejectedEvent, orderEntity);
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderCanceledEvent orderCanceledEvent) {
        log.debug("OrderEventHandler.OrderCanceledEvent for orderId: {}", orderCanceledEvent.getOrderId());
        OrderEntity orderEntity = orderRepository.findById(orderCanceledEvent.getOrderId()).get();
        orderEntity.setOrderStatus(orderCanceledEvent.getOrderStatus());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderUpdatedEvent orderUpdatedEvent) {
        log.debug("OrderEventHandler.OrderUpdatedEvent for orderId: {}", orderUpdatedEvent.getOrderId());
        OrderEntity orderEntity = orderRepository.findById(orderUpdatedEvent.getOrderId()).get();
        orderEntity.setOrderStatus(orderUpdatedEvent.getOrderStatus());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderPublishedEvent orderPublishedEvent) {
        try {
            String jsonValue = objectMapper.writeValueAsString(orderPublishedEvent);
            log.debug("OrderEventHandler.OrderPublishedEvent for orderId: {}, jsonValue: {}, kafkaTopic: {}",
                    orderPublishedEvent.getOrderId(), jsonValue, kafkaTopic);
            OrderEntity orderEntity = orderRepository.findById(orderPublishedEvent.getOrderId()).get();
            orderEntity.setOrderStatus(OrderStatus.PUBLISHED);
            orderRepository.save(orderEntity);
            kafkaTemplate.send(kafkaTopic, orderPublishedEvent.getOrderId(), jsonValue);
            kafkaTemplate.flush(); // to follow the progress on each message
        } catch (Exception ex) {
            log.error("OrderEventHandler.OrderPublishedEvent for orderId: {} got Exception: {}",
                    orderPublishedEvent.getOrderId(), ex.getMessage());
        }
    }

}
