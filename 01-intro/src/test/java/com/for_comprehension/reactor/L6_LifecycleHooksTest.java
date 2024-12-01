package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.for_comprehension.reactor.L6_LifecycleHooks.L1_newSubscriptionHook;
import static com.for_comprehension.reactor.L6_LifecycleHooks.L2_cancellationHook;
import static com.for_comprehension.reactor.L6_LifecycleHooks.L3_nextHook;
import static com.for_comprehension.reactor.L6_LifecycleHooks.L4_errorHook;
import static com.for_comprehension.reactor.L6_LifecycleHooks.L5_terminationHook;
import static com.for_comprehension.reactor.L6_LifecycleHooks.L6_finallyHook;
import static com.for_comprehension.reactor.L6_LifecycleHooks.L7_finallyHook;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class L6_LifecycleHooksTest {

    @Test
    public void testL1_newSubscriptionHook() {
        AtomicBoolean flag = new AtomicBoolean(false);

        Flux<Long> flux = L1_newSubscriptionHook(Flux.just(1L, 2L, 3L), flag);
        assertFalse(flag.get());

        flux.subscribe();

        assertTrue(flag.get());
    }

    @Test
    public void testL2_cancellationHook() {
        AtomicBoolean flag = new AtomicBoolean(false);

        Flux<Long> flux = L2_cancellationHook(Flux.just(1L, 2L, 3L), flag);
        assertFalse(flag.get());

        StepVerifier.create(flux)
          .thenCancel()
          .verify();

        assertTrue(flag.get());
    }

    @Test
    public void testL3_nextHook() {
        AtomicInteger counter = new AtomicInteger();

        Flux<Long> flux = L3_nextHook(Flux.just(1L, 2L, 3L), counter);
        assertEquals(0, counter.get());

        StepVerifier.create(flux)
          .expectNextCount(3)
          .verifyComplete();
    }

    @Test
    public void testL4_errorHook() {
        List<String> errors = new ArrayList<>();
        Flux<Long> errorNumbers = Flux.error(() -> new Exception("Error message"));

        Flux<Long> flux = L4_errorHook(errorNumbers, errors);
        assertTrue(errors.isEmpty());

        StepVerifier.create(flux)
          .verifyError();

        assertEquals(1, errors.size());
        assertEquals("Error message", errors.get(0));
    }

    @Test
    public void testL5_terminationHook() {
        AtomicBoolean flag = new AtomicBoolean(false);

        Flux<Long> flux = L5_terminationHook(Flux.just(1L, 2L, 3L), () -> flag.set(true));
        assertFalse(flag.get());

        StepVerifier.create(flux)
          .expectNext(1L, 2L, 3L)
          .verifyComplete();

        assertTrue(flag.get());
    }

    @Test
    public void testL6_finallyHook() {
        AtomicBoolean flag = new AtomicBoolean(false);

        Flux<Long> flux = L6_finallyHook(Flux.just(1L, 2L, 3L), signalType -> flag.set(true));
        assertFalse(flag.get());

        StepVerifier.create(flux)
          .expectNext(1L, 2L, 3L)
          .verifyComplete();

        assertTrue(flag.get());
    }

    @Test
    public void testL7_finallyHook() {
        List<Integer> values = new ArrayList<>();

        Flux<Long> flux = L7_finallyHook(Flux.just(1L, 2L, 3L), values);

        StepVerifier.create(flux)
          .expectNext(1L, 2L, 3L)
          .verifyComplete();

        assertEquals(Arrays.asList(1, 2, 3), values);
    }
}
