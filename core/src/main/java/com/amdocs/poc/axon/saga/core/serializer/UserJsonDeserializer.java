package com.amdocs.poc.axon.saga.core.serializer;

import com.amdocs.poc.axon.saga.core.model.Card;
import com.amdocs.poc.axon.saga.core.model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class UserJsonDeserializer extends StdDeserializer<User> {
    private static final long serialVersionUID = 5579141241817332594L;

    public UserJsonDeserializer() {
        this(null);
    }

    public UserJsonDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException, JsonProcessingException
    {
        final JsonNode node = jp.getCodec().readTree(jp);

        final String userId = node.get("userId").asText();
        final String firstName = node.get("firstName").asText();
        final String lastName = node.get("lastName").asText();
        final String cName = node.get("cName").asText();
        final String cCardNumber = node.get("cCardNumber").asText();
        final int cValidUntilMonth = (Integer) ((IntNode) node.get("cValidUntilMonth")).numberValue();
        final int cValidUntilYear = (Integer) ((IntNode) node.get("cValidUntilYear")).numberValue();
        final int cCvv = (Integer) ((IntNode) node.get("cCvv")).numberValue();

        Card card = Card.builder()
                .name(cName)
                .cardNumber(cCardNumber)
                .validUntilMonth(cValidUntilMonth)
                .validUntilYear(cValidUntilYear)
                .cvv(cCvv)
                .build();

        User user = User.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .card(card)
                .build();

        log.debug("UserJsonDeserializer.deserialize - User: {}", user);
        return user;
    }
}
