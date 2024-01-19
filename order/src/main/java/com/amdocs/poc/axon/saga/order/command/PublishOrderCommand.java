package com.amdocs.poc.axon.saga.order.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String userId;
}
