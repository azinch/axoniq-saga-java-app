package com.amdocs.poc.axon.saga.order.command;

import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private OrderStatus orderStatus;
}
