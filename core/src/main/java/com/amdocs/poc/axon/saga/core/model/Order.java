package com.amdocs.poc.axon.saga.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String orderId;
    private String userId;
    private String addressId;
    private String productId;
    private Integer quantity;
    private OrderStatus orderStatus;
}
