package com.amdocs.poc.axon.saga.order.data;

import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="axon_saga_order")
public class OrderEntity {
    @Id
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "address_id")
    private String addressId;
    @Column(name = "product_id")
    private String productId;
    private Integer quantity;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
