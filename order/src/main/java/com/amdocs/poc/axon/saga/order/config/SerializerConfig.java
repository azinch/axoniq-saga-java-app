package com.amdocs.poc.axon.saga.order.config;

import com.amdocs.poc.axon.saga.core.model.User;
import com.amdocs.poc.axon.saga.core.serializer.UserJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@Slf4j
public class SerializerConfig {

    /** @Bean
    @Qualifier("messageSerializer")
    public Serializer buildSerializer() {
        log.debug("SerializerConfig.messageSerializer.buildSerializer for order service");

        SimpleModule module = new SimpleModule("Custom", Version.unknownVersion());
        //module.addSerializer(User.class, new UserJsonSerializer());
        module.addDeserializer(User.class, new UserJsonDeserializer());

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(module)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return JacksonSerializer.builder()
                .objectMapper(objectMapper)
                .lenientDeserialization()
                .build();
    } **/
}
