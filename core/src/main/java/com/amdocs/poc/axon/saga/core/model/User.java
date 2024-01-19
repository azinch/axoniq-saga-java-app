package com.amdocs.poc.axon.saga.core.model;

import com.amdocs.poc.axon.saga.core.serializer.UserJsonDeserializer;
import com.amdocs.poc.axon.saga.core.serializer.UserJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonSerialize(using = UserJsonSerializer.class)
//@JsonDeserialize(using = UserJsonDeserializer.class)
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private Card card;
}
