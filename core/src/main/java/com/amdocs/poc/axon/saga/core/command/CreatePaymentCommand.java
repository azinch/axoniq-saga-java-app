package com.amdocs.poc.axon.saga.core.command;

import com.amdocs.poc.axon.saga.core.model.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private Card card;
}
