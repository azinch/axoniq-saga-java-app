package com.amdocs.poc.axon.saga.core.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShipmentCommand {
    @TargetAggregateIdentifier
    private String shipmentId;
    private String orderId;
}
