package com.amdocs.poc.axon.saga.shipment.event;

import com.amdocs.poc.axon.saga.core.event.ShipmentCreatedEvent;
import com.amdocs.poc.axon.saga.core.event.ShipmentRejectedEvent;
import com.amdocs.poc.axon.saga.shipment.data.ShipmentEntity;
import com.amdocs.poc.axon.saga.shipment.data.ShipmentRepository;
import com.amdocs.poc.axon.saga.shipment.model.ShipmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShipmentEventHandler {
    private ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(ShipmentCreatedEvent shipmentCreatedEvent) {
        log.debug("ShipmentEventHandler.ShipmentCreatedEvent for shipmentId: {}, orderId: {}",
                shipmentCreatedEvent.getShipmentId(), shipmentCreatedEvent.getOrderId());
        ShipmentEntity shipmentEntity =
                new ShipmentEntity(shipmentCreatedEvent.getShipmentId(),
                                  shipmentCreatedEvent.getOrderId(),
                                  ShipmentStatus.CREATED);
        shipmentRepository.save(shipmentEntity);
    }

    @EventHandler
    public void on(ShipmentRejectedEvent shipmentRejectedEvent) {
        ShipmentEntity shipmentEntity = new ShipmentEntity();
        shipmentEntity.setShipmentId(shipmentRejectedEvent.getShipmentId());
        shipmentEntity.setOrderId(shipmentRejectedEvent.getOrderId());
        shipmentEntity.setShipmentStatus(ShipmentStatus.REJECTED);
        shipmentRepository.save(shipmentEntity);
    }

}
