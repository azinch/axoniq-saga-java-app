package com.amdocs.poc.axon.saga.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private String userId;
    private String addressId;
    private String productId;
    private Integer quantity;
}
