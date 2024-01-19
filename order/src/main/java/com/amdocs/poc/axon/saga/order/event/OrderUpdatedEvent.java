package com.amdocs.poc.axon.saga.order.event;

import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdatedEvent {
    private String orderId;
    private OrderStatus orderStatus;
}
