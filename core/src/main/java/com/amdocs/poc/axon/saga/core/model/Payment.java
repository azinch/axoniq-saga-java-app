package com.amdocs.poc.axon.saga.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private String paymentId;
    private String orderId;
    private PaymentStatus paymentStatus;
}
