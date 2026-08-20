package com.oneenterprise.orderservice.config;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class CircuitBreaker {

    private enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    private State state = State.CLOSED;

    private int failureCount = 0;

    private final int failureThreshold = 3;

    private final Duration openDuration =
            Duration.ofSeconds(10);

    private Instant openedAt;

    public synchronized boolean allowRequest() {

        if (state == State.CLOSED) {
            return true;
        }

        if (state == State.OPEN) {

            if (openedAt != null &&
                    Duration.between(
                            openedAt,
                            Instant.now()
                    ).compareTo(openDuration) >= 0) {

                state = State.HALF_OPEN;

                return true;
            }

            return false;
        }

        // HALF_OPEN
        return true;
    }

    public synchronized void recordSuccess() {

        failureCount = 0;

        state = State.CLOSED;

        openedAt = null;
    }

    public synchronized void recordFailure() {

        failureCount++;

        if (failureCount >= failureThreshold) {

            state = State.OPEN;

            openedAt = Instant.now();
        }
    }

    public synchronized String getState() {

        return state.name();
    }

    public synchronized int getFailureCount() {

        return failureCount;
    }
}
