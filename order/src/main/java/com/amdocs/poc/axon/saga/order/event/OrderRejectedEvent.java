package com.amdocs.poc.axon.saga.order.event;

import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRejectedEvent {
    private String orderId;
    private String userId;
    private String addressId;
    private String productId;
    private Integer quantity;
    private OrderStatus orderStatus;
}
