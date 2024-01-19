package com.amdocs.poc.axon.saga.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplayOrderModel {
    private String processingGroup;
    private Long startPos;
}
