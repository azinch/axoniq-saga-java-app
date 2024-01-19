package com.amdocs.poc.axon.saga.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.extensions.kafka.eventhandling.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
public class AxonKafkaConfig {
    @Value("${axon.kafka.producer.bootstrap-servers}")
    String bootstrapServers;

    @Value("${axon.kafka.default-topic}")
    String topic;

    final private String PROCESSING_GROUP = "order-publishing";

    @Bean
    public ProducerFactory<String, byte[]> producerFactory(/*Map<String, Object> producerConfiguration*/)
    {
        log.info("AxonKafkaConfig.producerFactory with producerConfiguration: {} - Start",
                kafkaConfig()/*producerConfiguration*/);
        ProducerFactory<String, byte[]> producerFactory = DefaultProducerFactory.<String, byte[]>builder()
                //.configuration(producerConfiguration)
                .configuration(kafkaConfig())
                .build();
        log.info("AxonKafkaConfig.producerFactory created producerFactory: {} - End", producerFactory);
        return producerFactory;
    }

    @Bean
    public KafkaPublisher<String, byte[]> kafkaPublisher(ProducerFactory<String, byte[]> producerFactory)
    {
        log.info("AxonKafkaConfig.kafkaPublisher with producerFactory: {} - Start", producerFactory);
        KafkaPublisher<String, byte[]> kafkaPublisher = KafkaPublisher.<String, byte[]>builder()
                .topic(topic)
                .producerFactory(producerFactory)
                .build();
        log.info("AxonKafkaConfig.kafkaPublisher created kafkaPublisher: {} - End", kafkaPublisher);
        return kafkaPublisher;
    }

    @Bean
    public KafkaEventPublisher<String, byte[]> kafkaEventPublisher(KafkaPublisher<String, byte[]> kafkaPublisher)
    {
        log.info("AxonKafkaConfig.kafkaEventPublisher with kafkaPublisher: {} - Start", kafkaPublisher);
        KafkaEventPublisher<String, byte[]> kafkaEventPublisher = KafkaEventPublisher.<String, byte[]>builder()
                .kafkaPublisher(kafkaPublisher)
                .build();
        log.info("AxonKafkaConfig.kafkaEventPublisher created kafkaEventPublisher: {} - End", kafkaEventPublisher);
        return kafkaEventPublisher;
    }

    @Autowired
    public void registerPublisherToEventProcessor(EventProcessingConfigurer eventProcessingConfigurer,
                                                  KafkaEventPublisher<String, byte[]> kafkaEventPublisher)
    {
        log.info("AxonKafkaConfig.registerPublisherToEventProcessor with kafkaEventPublisher: {} - Start",
                kafkaEventPublisher);
        //String processingGroup = KafkaEventPublisher.DEFAULT_PROCESSING_GROUP;
        eventProcessingConfigurer.registerEventHandler(configuration -> kafkaEventPublisher)
                .assignHandlerTypesMatching(PROCESSING_GROUP,
                        clazz -> clazz.isAssignableFrom(KafkaEventPublisher.class))
                .registerTrackingEventProcessor(PROCESSING_GROUP);
                //.registerSubscribingEventProcessor(PROCESSING_GROUP);
        log.info("AxonKafkaConfig.registerPublisherToEventProcessor created eventProcessingConfigurer: {} - End",
                eventProcessingConfigurer);
    }

    private Map<String, Object> kafkaConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.RETRIES_CONFIG, 0);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return config;
    }

}
