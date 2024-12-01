package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxBufferTest {

    @Test
    void L1_buffer() {
        Flux<Integer> numbers = Flux.range(1, 10);
        Flux<List<Integer>> bufferedFlux = L12_Batching.L1_buffer(numbers, 3);
        StepVerifier.create(bufferedFlux)
            .expectNext(Arrays.asList(1, 2, 3))
            .expectNext(Arrays.asList(4, 5, 6))
            .expectNext(Arrays.asList(7, 8, 9))
            .expectNext(Arrays.asList(10))
            .verifyComplete();
    }

    @Test
    void L2_bufferOrTimeout() {
        Flux<Integer> numbers = Flux.range(1, 10).delayElements(Duration.ofMillis(10));
        Flux<List<Integer>> bufferedFlux = L12_Batching.L2_bufferOrTimeout(numbers, 3, Duration.ofMillis(50));
        StepVerifier.create(bufferedFlux)
            .expectNext(Arrays.asList(1, 2, 3))
            .expectNext(Arrays.asList(4, 5, 6))
            .expectNext(Arrays.asList(7, 8, 9))
            .expectNext(Arrays.asList(10))
            .verifyComplete();
    }

    @Test
    void L3_overlappingBuffer() {
        Flux<Integer> numbers = Flux.range(1, 9);
        Flux<List<Integer>> bufferedFlux = L12_Batching.L3_overlappingBuffer(numbers, 5, 3);
        StepVerifier.create(bufferedFlux)
            .expectNext(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5)))
            .expectNext(new LinkedList<>(Arrays.asList(4, 5, 6, 7, 8)))
            .expectNext(new LinkedList<>(Arrays.asList(7, 8, 9)))
            .verifyComplete();
    }
}
