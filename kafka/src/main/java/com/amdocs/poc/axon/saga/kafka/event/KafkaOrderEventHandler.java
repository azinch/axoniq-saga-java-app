package com.amdocs.poc.axon.saga.kafka.event;

import com.amdocs.poc.axon.saga.core.event.OrderPublishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup(value = "order-publishing")
@Slf4j
public class KafkaOrderEventHandler {
    @EventHandler
    public void on(OrderPublishedEvent orderPublishedEvent) {
        log.debug("KafkaOrderEventHandler.OrderPublishedEvent for orderId: {}", orderPublishedEvent.getOrderId());
    }
}
