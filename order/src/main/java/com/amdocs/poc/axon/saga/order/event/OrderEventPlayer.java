package com.amdocs.poc.axon.saga.order.event;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.*;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.IntStream;


@Component
@Slf4j
public class OrderEventPlayer {
    private EventProcessingConfiguration eventProcConf;
    private TokenStore tokenStore;

    public OrderEventPlayer() {
        log.info("OrderEventPlayer default constructor");
    }

    @Autowired
    public OrderEventPlayer(EventProcessingConfiguration eventProcConf, TokenStore tokenStore) {
        this.eventProcConf = eventProcConf;
        this.tokenStore = tokenStore;
        log.info("OrderEventPlayer constructor for eventProcConf: {}, tokenStore: {}", eventProcConf, tokenStore);
    }

    public void replay(String processingGroup, Long startPos) {
        log.debug("EventPlayer.replay for processingGroup: {}, startPos: {} - Start",
                processingGroup, startPos);
        eventProcConf.eventProcessor(processingGroup, TrackingEventProcessor.class)
                .ifPresent(processor -> {
                    log.debug("EventPlayer.replay Processor for processingGroup: {} Found!", processingGroup);
                    processor.shutDown();
                    //processor.resetTokens();
                    //processor.resetTokens((GapAwareTrackingToken.newInstance(startPos - 1, Collections.emptySortedSet())));
                    processor.resetTokens(new GlobalSequenceTrackingToken(startPos - 1));
                    processor.start();
                });
        log.debug("EventPlayer.replay for processingGroup: {}, startPos: {} - End",
                processingGroup, startPos);
    }

    @Transactional
    public Optional<Progress> getProgress(String processingGroup)
    {
        log.debug("EventPlayer.getProgress for processingGroup: {} - Start", processingGroup);
        Optional<Progress> curProgress = Optional.empty();

        int[] segments = tokenStore.fetchSegments(processingGroup);
        if (segments.length != 0)
        {
            Progress accumulatedProgress = IntStream.of(segments).mapToObj(segment -> {
                TrackingToken token = tokenStore.fetchToken(processingGroup, segment);

                OptionalLong maybeCurrent = token.position();
                OptionalLong maybePositionAtReset = OptionalLong.empty();

                if (token instanceof ReplayToken) {
                    maybePositionAtReset = ((ReplayToken) token).getTokenAtReset().position();
                }

                return new Progress(maybeCurrent.orElse(0L), maybePositionAtReset.orElse(0L));
            }).reduce(new Progress(0, 0), (acc, progress) ->
                    new Progress(acc.getCurrent() + progress.getCurrent(), acc.getTail() + progress.getTail()));

            curProgress = (accumulatedProgress.getTail() == 0L) ? Optional.empty() : Optional.of(accumulatedProgress);
        }

        log.debug("EventPlayer.getProgress for processingGroup: {} calculated Progress: {} - End",
                processingGroup, curProgress);

        return curProgress;
    }

    @Value
    public static class Progress {
        private long current;
        private long tail;

        public BigDecimal getProgress() {
            return BigDecimal.valueOf(current, 2).divide(BigDecimal.valueOf(tail, 2), RoundingMode.HALF_UP);
        }
    }

}
