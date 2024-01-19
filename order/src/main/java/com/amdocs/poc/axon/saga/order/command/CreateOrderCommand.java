package com.amdocs.poc.axon.saga.order.command;

import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String userId;
    private String addressId;
    private String productId;
    private Integer quantity;
    private OrderStatus orderStatus;
}
