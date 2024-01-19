package com.amdocs.poc.axon.saga.order.config;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class AxonConfig {
    @Value("${order.processing.group}")
    private String processingGroup;

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        log.info("AxonConfig.EventProcessingConfigurer for processingGroup: {}", processingGroup);
        configurer.byDefaultAssignTo(processingGroup);
        //configurer.usingTrackingEventProcessors().byDefaultAssignTo(processingGroup);
    }


}
