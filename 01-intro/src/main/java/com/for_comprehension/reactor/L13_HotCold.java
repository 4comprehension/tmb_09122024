package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

class L13_HotCold {

    /**
     * Provided Flux fails from time to timed
     */
    static Flux<List<Integer>> L2_bufferOrTimeout(Flux<Integer> numbers, int n, Duration timeout) {
        return numbers.bufferTimeout(n, timeout);
    }

    public static void main(String[] args) throws InterruptedException {
    }
}
