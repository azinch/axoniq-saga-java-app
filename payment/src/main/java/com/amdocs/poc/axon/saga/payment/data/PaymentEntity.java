package com.amdocs.poc.axon.saga.payment.data;

import com.amdocs.poc.axon.saga.core.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "axon_saga_payment")
public class PaymentEntity {
    @Id
    @Column(name = "payment_id")
    private String paymentId;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "timestamp")
    private Date timeStamp;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
