package com.amdocs.poc.axon.saga.user.event;

import com.amdocs.poc.axon.saga.core.model.Card;
import com.amdocs.poc.axon.saga.core.model.User;
import com.amdocs.poc.axon.saga.core.query.UserQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserProjector {
    public UserProjector() {
        log.debug("UserProjector.constructor");
    }

    @QueryHandler
    public User getUser(UserQuery query) {
        log.debug("UserProjector.getUser");
        Card card = Card.builder()
                .name("andreyz")
                .cardNumber("123456789")
                .validUntilMonth(04)
                .validUntilYear(2023)
                .cvv(999)
                .build();
        User user = User.builder()
                .userId(query.getUserId())
                .firstName("Andrey")
                .lastName("Zinchenko")
                .card(card)
                .build();
        log.debug("UserProjector.getUser for query: {}\n\tUser retrieved: {}", query, user);
        return user;
    }

}
