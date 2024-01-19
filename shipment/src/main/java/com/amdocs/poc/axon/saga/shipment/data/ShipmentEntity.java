package com.amdocs.poc.axon.saga.shipment.data;

import com.amdocs.poc.axon.saga.shipment.model.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "axon_saga_shipment")
public class ShipmentEntity {
    @Id
    @Column(name = "shipment_id")
    private String shipmentId;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "shipment_status")
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
}
