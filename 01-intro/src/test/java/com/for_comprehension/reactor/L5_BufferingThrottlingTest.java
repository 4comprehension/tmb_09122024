package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

class L5_BufferingThrottlingTest {

    @Test
    void testL2_delay() {
        Flux<Integer> input = Flux.just(1, 2, 3);
        Flux<Integer> result = L5_BufferingThrottling.L2_delay(input);

        StepVerifier.create(result)
          .expectSubscription()
          .expectNext(1)
          .expectNoEvent(Duration.ofMillis(900))
          .expectNext(2)
          .expectNoEvent(Duration.ofMillis(900))
          .expectNext(3)
          .expectComplete()
          .verify();
    }

    @Test
    void testL3_buffer() {
        Flux<Integer> input = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        int bufferSize = 3;
        Flux<List<Integer>> result = L5_BufferingThrottling.L3_buffer(input, bufferSize);

        StepVerifier.create(result)
          .expectNext(List.of(1, 2, 3))
          .expectNext(List.of(4, 5, 6))
          .expectNext(List.of(7, 8, 9))
          .expectComplete()
          .verify();
    }

    @Test
    void testL5_sample() {
        Flux<Integer> input = Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofMillis(500));
        Flux<Integer> result = L5_BufferingThrottling.L5_sample(input);

        StepVerifier.create(result)
          .expectNext(1, 3, 4, 6)
          .expectComplete()
          .verify();
    }

    @Test
    void testL6_delaySubscription() {
        var input = Flux.just(1, 2, 3);
        var delay = Duration.ofSeconds(1);
        var result = L5_BufferingThrottling.L6_delaySubscription(input, delay);

        StepVerifier.create(result)
          .expectSubscription()
          .expectNoEvent(delay)
          .expectNext(1, 2, 3)
          .expectComplete()
          .verify();
    }

    @Test
    void testL7_slidingWindowBuffer() {
        Flux<Integer> input = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        int maxSize = 5;
        int skip = 3;
        Flux<List<Integer>> result = L5_BufferingThrottling.L7_slidingWindowBuffer(input, maxSize, skip);

        StepVerifier.create(result)
          .expectNext(List.of(1, 2, 3, 4, 5))
          .expectNext(List.of(4, 5, 6, 7, 8))
          .expectNext(List.of(7, 8, 9))
          .expectComplete()
          .verify();
    }
}
