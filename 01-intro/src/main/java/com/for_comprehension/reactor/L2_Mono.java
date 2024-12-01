package com.for_comprehension.reactor;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

class L2_Mono {

    /**
     * Create an empty {@link Mono}
     */
    static <T> Mono<T> L0_createEmptyMono() {
        return Mono.empty();
    }

    /**
     * Create a {@link Mono} with provided value
     *
     * @param value value to be returned by {@link Mono}
     */
    static Mono<Integer> L1_createEagerMono(int value) {
        return Mono.just(value);
    }

    /**
     * Create a {@link Mono} that computes value lazily by incrementing the counter and returning the incremented value
     */
    static Mono<Integer> L2_createLazyMono(AtomicInteger counter) {
        return Mono.fromCallable(counter::incrementAndGet);
    }

    /**
     * Create a {@link Mono} that computes value lazily by incrementing the counter and returning the incremented value
     *
     * @implNote the value of the Mono needs to be cached so that subsequent calls do not result in counter increment
     */
    static Mono<Integer> L3_createLazyMonoAndCache(AtomicInteger counter) {
        return Mono.fromCallable(counter::incrementAndGet).cache();
    }

    /**
     * Create a {@link Mono} that computes value lazily by incrementing the counter and returning the incremented value
     *
     * @implNote the value of the Mono needs to be cached so that subsequent calls do not result in counter increment (ttl should be set to a provided value)
     */
    static Mono<Integer> L4_createLazyMonoAndCacheTTL(AtomicInteger counter, int seconds) {
        return Mono.fromCallable(counter::incrementAndGet).cache(Duration.ofSeconds(2));
    }

    /**
     * Get value from a {@link Mono}
     *
     * @implNote don't worry about blocking, it's fine (for now :) )
     */
    static Integer L5_getValue(Mono<Integer> mono) {
        return mono.block();
    }

    /**
     * Get value from a {@link Mono} or throw an exception after provided timeout
     *
     * @implNote don't worry about blocking, it's fine (for now :) )
     */
    static Integer L6_getValueWithTimeout(Mono<Integer> mono, int millis) {
        return mono.block(Duration.ofMillis(millis));
    }

    /**
     * Get value from a {@link Mono} or return the fallbackValue after provided timeout
     *
     * @implNote don't worry about blocking, it's fine (for now :) )
     */
    static Integer L7_getValueWithTimeoutOrElse(Mono<Integer> mono, int seconds, int fallbackValue) {
        return mono.timeout(Duration.ofSeconds(seconds), Mono.just(fallbackValue)).block();
    }

    /**
     * Get the value from a {@link Mono} that successfully returned a value first
     */
    static Mono<Integer> L8_getCompletedFirst(Mono<Integer> m1, Mono<Integer> m2) {
        return Mono.firstWithValue(m1, m2);
    }
}
