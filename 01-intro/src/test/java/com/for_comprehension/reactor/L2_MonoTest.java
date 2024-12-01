package com.for_comprehension.reactor;

import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @implNote yes, this is not how you're supposed to test reactive code. Do not worry, you will have a chance to rewrite those later :)
 */
class L2_MonoTest {

    @Test
    void test_L0_createEmptyMono() {
        assertNull(L2_Mono.L0_createEmptyMono().block());
    }

    @Test
    void test_L1_createEagerMono() {
        int value = 5;
        assertEquals(value, L2_Mono.L1_createEagerMono(value).block());
    }

    @Test
    void test_L2_createLazyMono() {
        AtomicInteger counter = new AtomicInteger(0);
        var mono = L2_Mono.L2_createLazyMono(counter);
        assertEquals(1, mono.block());
        assertEquals(1, counter.get());
        assertEquals(2, mono.block());
        assertEquals(2, counter.get());
    }

    @Test
    void test_L3_createLazyMonoAndCache() {
        var counter = new AtomicInteger(0);
        var mono = L2_Mono.L3_createLazyMonoAndCache(counter);
        assertEquals(1, mono.block());
        assertEquals(1, mono.block());
        assertEquals(1, counter.get());
    }

    @Test
    void test_L4_createLazyMonoAndCacheTTL() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        Mono<Integer> mono = L2_Mono.L4_createLazyMonoAndCacheTTL(counter, 2);
        assertEquals(1, mono.block());
        Awaitility.await()
          .during(Duration.ofMillis(1000))
          .until(() -> mono.block() == 1);

        Awaitility.await()
          .until(() -> mono.block() > 1);
    }

    @Test
    void test_L5_getValue() {
        assertEquals(5, L2_Mono.L5_getValue(Mono.just(5)));
    }

    @Test
    void test_L6_getValueWithTimeout() {
        Assertions.assertThatThrownBy(() -> {
            L2_Mono.L6_getValueWithTimeout(Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(Duration.ofSeconds(60).toMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return 42;
            }, Executors.newSingleThreadExecutor())), 1000);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void test_L7_getValueWithTimeoutOrElse() {
        assertEquals(5, L2_Mono.L7_getValueWithTimeoutOrElse(Mono.just(5), 5, 10));
        assertEquals(10, L2_Mono.L7_getValueWithTimeoutOrElse(Mono.delay(Duration.ofSeconds(5)).map(l -> 100), 2, 10));
    }

    @Test
    void test_L8_getCompletedFirst() {
        Integer block = L2_Mono.L8_getCompletedFirst(Mono.just(42), Mono.just(1).delayElement(Duration.ofSeconds(1)))
          .block();

        assertEquals(42, block);
    }
}
