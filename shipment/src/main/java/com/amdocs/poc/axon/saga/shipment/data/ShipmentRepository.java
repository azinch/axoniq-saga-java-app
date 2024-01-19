package com.amdocs.poc.axon.saga.shipment.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity, String> {
}
