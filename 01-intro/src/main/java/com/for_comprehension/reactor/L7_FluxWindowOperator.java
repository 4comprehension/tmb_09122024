package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

class L7_FluxWindowOperator {

    /**
     * Transform the flux into a flux of windows of size 3
     */
    static Flux<Flux<Integer>> E1_windowBySize(Flux<Integer> numbers) {
        return numbers.window(3);
    }

    /**
     * Transform the flux into a flux of windows of duration 1 second
     */
    static Flux<Flux<Long>> E2_windowByTime(Flux<Long> ticks) {
        return ticks.window(Duration.ofSeconds(1));
    }

    /**
     * Transform the flux into a flux of windows of size 3, but do not wait more than 1s for those 3 items to arrive, then collect each group to list
     */
    static Flux<List<Integer>> E3_windowBySizeAndTime(Flux<Integer> numbers, int timeoutSeconds) {
        return numbers.windowTimeout(3, Duration.ofSeconds(timeoutSeconds)).flatMap(g -> g.collectList());
    }

    /**
     * Split the flux into windows each time the predicate is met (value is even)
     */
    static Flux<Flux<Integer>> E4_windowAsLongAsEven(Flux<Integer> numbers) {
        return numbers.windowUntil(i -> i % 2 == 0);
    }

    /**
     * Split the flux into windows while the predicate is met (value is negative)
     */
    static Flux<List<Integer>> E5_windowAsLongAsNegative(Flux<Integer> numbers) {
        return numbers.windowWhile(i -> i < 0).flatMap(c -> c.collectList());
    }

    /**
     * Create windows lasting 5 seconds and return a Flux<Integer> of the size of each window
     */
    static Flux<Long> E6_windowCount(Flux<Long> ticks) {
        return ticks.window(Duration.ofSeconds(5)).flatMap(window -> window.count());
    }
}

