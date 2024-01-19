package com.amdocs.poc.axon.saga.core.serializer;

import com.amdocs.poc.axon.saga.core.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class UserJsonSerializer extends StdSerializer<User> {
    private static final long serialVersionUID = 6739170890621978901L;

    public UserJsonSerializer() {
        this(null);
    }

    public UserJsonSerializer(final Class<User> t) {
        super(t);
    }

    @Override
    public final void serialize(final User value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        log.debug("UserJsonSerializer.serialize - User: {}", value);

        jgen.writeStartObject();
        jgen.writeStringField("userId", value.getUserId());
        jgen.writeStringField("firstName", value.getFirstName());
        jgen.writeStringField("lastName", value.getLastName());
        jgen.writeStringField("cName", value.getCard().getName());
        jgen.writeStringField("cCardNumber", value.getCard().getCardNumber());
        jgen.writeNumberField("cValidUntilMonth", value.getCard().getValidUntilMonth());
        jgen.writeNumberField("cValidUntilYear", value.getCard().getValidUntilYear());
        jgen.writeNumberField("cCvv", value.getCard().getCvv());
        jgen.writeEndObject();
    }
}
