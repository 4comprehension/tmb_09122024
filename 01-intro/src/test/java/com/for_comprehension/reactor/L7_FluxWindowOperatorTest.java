package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static com.for_comprehension.reactor.L7_FluxWindowOperator.E5_windowAsLongAsNegative;
import static com.for_comprehension.reactor.L7_FluxWindowOperator.E6_windowCount;

class L7_FluxWindowOperatorTest {

    @Test
    void E1_windowBySize() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6, 7);
        StepVerifier.create(L7_FluxWindowOperator.E1_windowBySize(flux).flatMap(Flux::collectList))
          .expectNext(List.of(1, 2, 3))
          .expectNext(List.of(4, 5, 6))
          .expectNext(List.of(7))
          .verifyComplete();
    }

    @Test
    void E2_windowByTime_TestWithVirtualTime() {
        StepVerifier.withVirtualTime(() -> L7_FluxWindowOperator.E2_windowByTime(Flux.interval(Duration.ofMillis(100)))
            .take(3))
          .thenAwait(Duration.ofSeconds(1))
          .expectNextCount(1)
          .thenAwait(Duration.ofSeconds(1))
          .expectNextCount(1)
          .thenAwait(Duration.ofSeconds(1))
          .expectNextCount(1)
          .thenAwait(Duration.ofSeconds(1))
          .verifyComplete();
    }

    @Test
    void E3_windowBySizeAndTime() {
        StepVerifier.withVirtualTime(() -> {
              return L7_FluxWindowOperator.E3_windowBySizeAndTime(Flux.just(1, 2, 3)
                .concatWith(Flux.just(4, 5).delayElements(Duration.ofSeconds(2))), 3);
          })
          .thenAwait(Duration.ofSeconds(1))
          .expectNext(List.of(1, 2, 3))
          .thenAwait(Duration.ofSeconds(2))
          .expectNext(List.of(4))
          .thenAwait(Duration.ofSeconds(2))
          .expectNext(List.of(5))
          .verifyComplete();
    }

    @Test
    void E4_windowAsLongAsEven() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6, 7);
        StepVerifier.create(L7_FluxWindowOperator.E4_windowAsLongAsEven(flux).flatMap(Flux::collectList))
          .expectNext(List.of(1, 2))
          .expectNext(List.of(3, 4))
          .expectNext(List.of(5, 6))
          .expectNext(List.of(7))
          .verifyComplete();
    }

    @Test
    void E5_windowAsLongAsNegative() {
        Flux<Integer> numbers = Flux.just(-1, -2, -3, 4, -5, -6, 7);
        Flux<List<Integer>> result = L7_FluxWindowOperator.E5_windowAsLongAsNegative(numbers);

        StepVerifier.create(result)
          .expectNext(List.of(-1, -2, -3))
          .expectNext(List.of(-5, -6))
          .verifyComplete();
    }

    @Test
    void E6_windowCount() {
        StepVerifier.withVirtualTime(() -> L7_FluxWindowOperator.E6_windowCount(Flux.interval(Duration.ofMillis(800)).take(10)))
          .thenAwait(Duration.ofSeconds(6))
          .expectNext(6L)
          .thenAwait(Duration.ofSeconds(2))
          .expectNext(4L)
          .verifyComplete();
    }
}
