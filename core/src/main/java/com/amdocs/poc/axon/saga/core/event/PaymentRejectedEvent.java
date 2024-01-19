package com.amdocs.poc.axon.saga.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRejectedEvent {
    private String paymentId;
    private String orderId;
}
