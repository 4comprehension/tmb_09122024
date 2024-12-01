package com.for_comprehension.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BufferTest {

    @Test
    public void L1_bufferTest() {
        Flux<Integer> fluxInts = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Flux<List<Integer>> fluxLists = L12_Batching.L1_buffer(fluxInts, 3);

        StepVerifier.create(fluxLists)
          .expectNext(List.of(1, 2, 3))
          .expectNext(List.of(4, 5, 6))
          .expectNext(List.of(7, 8, 9))
          .verifyComplete();
    }

    @Test
    public void L2_bufferOrTimeoutTest() {
        Flux<Integer> fluxInts = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
          .delayElements(Duration.ofMillis(10))
          .concatWith(Flux.just(10, 11));

        Flux<List<Integer>> fluxLists = L12_Batching.L2_bufferOrTimeout(fluxInts, 5, Duration.ofMillis(25));

        StepVerifier.create(fluxLists)
          .expectNext(List.of(1, 2, 3))
          .expectNext(List.of(4, 5, 6))
          .expectNext(List.of(7, 8, 9, 10, 11))
          .verifyComplete();
    }

    @Test
    public void L3_overlappingBufferTest() {
        Flux<Integer> fluxInts = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Flux<List<Integer>> fluxLists = L12_Batching.L3_overlappingBuffer(fluxInts, 5, 3);

        StepVerifier.create(fluxLists)
          .expectNext(List.of(1, 2, 3, 4, 5))
          .expectNext(List.of(4, 5, 6, 7, 8))
          .expectNext(List.of(7, 8, 9))
          .verifyComplete();
    }
}
