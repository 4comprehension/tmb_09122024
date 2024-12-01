package com.for_comprehension.reactor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.for_comprehension.reactor.L3_Flux.L11_repeatMono;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @implNote yes, this is not how you're supposed to test reactive code. Do not worry, you will have a chance to rewrite those later :)
 */
class L3_FluxTest {

    @Test
    void L0_createEmptyFlux() {
        assertTrue(L3_Flux.L0_createEmptyFlux().collectList().block().isEmpty());
    }

    @Test
    void L1_createEagerFlux() {
        AtomicInteger counter = new AtomicInteger(0);
        assertEquals(List.of(1, 2, 3), L3_Flux.L1_createEagerFlux(counter).collectList().block());
    }

    @Test
    void L2_createLazyFlux() {
        AtomicInteger counter = new AtomicInteger(0);
        assertEquals(List.of(1, 2, 3), L3_Flux.L2_createLazyFlux(counter).collectList().block());
    }

    @Test
    void L3_createLazyMonoAndCache() {
        AtomicInteger counter = new AtomicInteger(0);
        var result = L3_Flux.L3_createLazyFluxAndCache(counter);
        assertEquals(List.of(1, 2, 3), result.collectList().block());
        assertEquals(List.of(1, 2, 3), result.collectList().block());
    }

    @Test
    void L4_createLazyMonoAndCacheWithTTL() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        var flux = L3_Flux.L4_createLazyFluxAndCacheWithTTL(counter, 100);

        assertEquals(List.of(1, 2, 3), flux.collectList().block());
        Thread.sleep(200);
        assertEquals(List.of(4, 5, 6), flux.collectList().block());
    }

    @Test
    void L5_getValue() {
        List<Integer> values = List.of(1, 2, 3);
        var flux = Flux.fromIterable(values);
        assertEquals(values, L3_Flux.L5_getValue(flux));
    }

    @Test
    void L6_getValueWithTimeout() {
        List<Integer> values = List.of(1, 2, 3);
        var flux = Flux.fromIterable(values);
        var result = L3_Flux.L6_getValueWithTimeout(flux, 1000);
        assertEquals(values, result);

        var longFlux = Flux.fromIterable(values).delayElements(Duration.ofSeconds(2));
        assertThatThrownBy(() -> assertNull(L3_Flux.L6_getValueWithTimeout(longFlux, 1000))).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void L7_getValueWithTimeoutOrElse() {
        List<Integer> values = List.of(1, 2, 3);
        var flux = Flux.fromIterable(values);
        assertEquals(values, L3_Flux.L7_getValueWithTimeoutOrElse(flux, 1, 0));

        var longFlux = Flux.fromIterable(values).delayElements(Duration.ofSeconds(2));
        assertEquals(List.of(0), L3_Flux.L7_getValueWithTimeoutOrElse(longFlux, 1, 0));
    }

    @Test
    void L8_getFirstValue() {
        List<Integer> values = List.of(1, 2, 3);
        var flux = Flux.fromIterable(values);
        assertEquals(1, L3_Flux.L8_getFirstValue(flux));
    }

    @Test
    void L9_fixMissingValues() throws Exception{
        List<Integer> result = L3_Flux.L9_fixMissingValues(Flux.fromIterable(List.of(1, 2, 3)));

        Assertions.assertThat(result).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    void L10_fixMissingValues() throws Exception {
        List<Integer> result = L3_Flux.L10_fixMissingValues(Flux.fromIterable(List.of(1, 2, 3)));

        Assertions.assertThat(result).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    void testL11_repeatMono() {
        Mono<Integer> mono = Mono.just(5);
        Flux<Integer> result = L11_repeatMono(mono, 3);

        StepVerifier.create(result)
          .expectNext(5)
          .expectNext(5)
          .expectNext(5)
          .expectNext(5)
          .verifyComplete();
    }
}
