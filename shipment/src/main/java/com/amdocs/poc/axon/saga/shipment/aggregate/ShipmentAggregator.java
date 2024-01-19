package com.amdocs.poc.axon.saga.shipment.aggregate;

import com.amdocs.poc.axon.saga.core.command.CreateShipmentCommand;
import com.amdocs.poc.axon.saga.core.event.ShipmentCreatedEvent;
import com.amdocs.poc.axon.saga.core.event.ShipmentRejectedEvent;
import com.amdocs.poc.axon.saga.shipment.model.ShipmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import java.util.Random;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class ShipmentAggregator {
    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private ShipmentStatus shipmentStatus;

    private Random random = new Random();

    public ShipmentAggregator() {
    }

    @CommandHandler
    public ShipmentAggregator(CreateShipmentCommand createShipmentCommand) {
        int intRandom = random.nextInt(100);
        log.debug("ShipmentAggregator.CreateShipmentCommand for shipmentId: {}, orderId: {} with intRandom: {}",
                createShipmentCommand.getShipmentId(), createShipmentCommand.getOrderId(), intRandom);
        //Validations and rest of the Shipment business logic should be put here..
        if((intRandom % 2) != 0) {
            ShipmentCreatedEvent shipmentCreatedEvent =
                    new ShipmentCreatedEvent(createShipmentCommand.getShipmentId(), createShipmentCommand.getOrderId());
            apply(shipmentCreatedEvent);
        } else {
            ShipmentRejectedEvent shipmentRejectedEvent =
                    new ShipmentRejectedEvent(createShipmentCommand.getShipmentId(), createShipmentCommand.getOrderId());
            apply(shipmentRejectedEvent);
        }

    }

    @EventSourcingHandler
    public void on(ShipmentCreatedEvent shipmentCreatedEvent) {
        this.shipmentId = shipmentCreatedEvent.getShipmentId();
        this.orderId = shipmentCreatedEvent.getOrderId();
        this.shipmentStatus = ShipmentStatus.CREATED;
        log.debug("EventSourcingHandler.ShipmentCreatedEvent with shipmentId: {}, orderId: {}", shipmentId, orderId);
    }

    @EventSourcingHandler
    public void on(ShipmentRejectedEvent shipmentRejectedEvent) {
        this.shipmentId = shipmentRejectedEvent.getShipmentId();
        this.orderId = shipmentRejectedEvent.getOrderId();
        this.shipmentStatus = ShipmentStatus.REJECTED;
        log.debug("EventSourcingHandler.ShipmentRejectedEvent with shipmentId: {}, orderId: {}", shipmentId, orderId);
    }

}
