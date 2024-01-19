package com.amdocs.poc.axon.saga.user.config;

import com.amdocs.poc.axon.saga.core.model.User;
import com.amdocs.poc.axon.saga.core.serializer.UserJsonSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@Slf4j
public class ObjectMapperConfig {

    /** @Bean
    public ObjectMapper objectMapper()
    {
        log.debug("ObjectMapperConfig.objectMapper for user service");
        
        SimpleModule module = new SimpleModule("Custom", Version.unknownVersion());
        module.addSerializer(User.class, new UserJsonSerializer());
        //module.addDeserializer(User.class, new UserJsonDeserializer());

        return new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module);
    } **/
}
