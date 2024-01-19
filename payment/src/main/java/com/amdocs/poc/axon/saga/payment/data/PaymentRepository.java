package com.amdocs.poc.axon.saga.payment.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    List<PaymentEntity> findByOrderId(String orderId);
    //oracle native query
    @Query(value = "select * from axon_saga_payment t where t.order_id = :orderid", nativeQuery = true)
    PaymentEntity findByOrderIdNative(@Param("orderid") String orderId);

}
