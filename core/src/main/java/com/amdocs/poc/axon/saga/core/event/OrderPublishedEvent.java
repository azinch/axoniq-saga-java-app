package com.amdocs.poc.axon.saga.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPublishedEvent {
    private String orderId;
    private String userId;
}
