package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;

class L14_Retries {

    /**
     * Process provided Flux with a given reactive toString function. Note that the implementation is flaky, so keep repeating until it works just fine
     *
     * @implNote don't use infinite retries on production
     */
    static Flux<String> L1_repeatUntilSuccess(Flux<Integer> flux, Function<Integer, Mono<String>> toStringAsync) {
        return flux.flatMap(toStringAsync.andThen(Mono::retry));
    }

    /**
     * Provided Mono usually returns response "immediately" but sometimes it gets stuck
     * and it's better to retry than to hope it finally returns
     * <p>
     * If the processing lasts more than provided timeout, try again once
     */
    static Mono<Integer> L2_retryIfSlow(Mono<Integer> source, Duration timeout) {
        return source.timeout(timeout).retry();
    }
}
