package com.amdocs.poc.axon.saga.order.event;

import com.amdocs.poc.axon.saga.core.model.Order;
import com.amdocs.poc.axon.saga.core.query.OrderQuery;
import com.amdocs.poc.axon.saga.order.data.OrderEntity;
import com.amdocs.poc.axon.saga.order.data.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProjector {
    private OrderRepository orderRepository;

    @Autowired
    public OrderProjector(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @QueryHandler
    public Order getOrder(OrderQuery orderQuery) {
        log.debug("OrderProjector.getOrder");

        OrderEntity orderEntity = orderRepository.findById(orderQuery.getOrderId()).get();
        Order order = Order.builder()
                .orderId(orderEntity.getOrderId())
                .userId(orderEntity.getUserId())
                .addressId(orderEntity.getAddressId())
                .productId(orderEntity.getProductId())
                .quantity(orderEntity.getQuantity())
                .orderStatus(orderEntity.getOrderStatus())
                .build();

        log.debug("OrderProjector.getOrder for query: {}\n\tOrderEntity retrieved: {}\n\tOrder extracted: {}",
                orderQuery, orderEntity, order);

        return order;
    }
}
