package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.for_comprehension.reactor.L4_Transformations.L12_convertSignalsIntoItems;
import static com.for_comprehension.reactor.L4_Transformations.L13_zipWithIndex;
import static com.for_comprehension.reactor.L4_Transformations.L14_wordCount;
import static com.for_comprehension.reactor.L4_Transformations.L15_extractPartialSums;

class L4_TransformationsTest {

    @Test
    void testL0_increment() {
        Flux<Integer> input = Flux.just(1, 2, 3);
        Flux<Integer> result = L4_Transformations.L0_incrementAll(input);

        StepVerifier.create(result)
          .expectNext(2, 3, 4)
          .expectComplete()
          .verify();
    }

    @Test
    void testL1_cast() {
        Flux<Object> input = Flux.just(1, 2, 3);
        Flux<Integer> result = L4_Transformations.L1_castToInteger(input);

        StepVerifier.create(result)
          .expectNext(1, 2, 3)
          .expectComplete()
          .verify();
    }

    @Test
    void testL2_sum() {
        Flux<Integer> input = Flux.just(1, 2, 3);
        Mono<Integer> result = L4_Transformations.L2_sumAll(input);

        StepVerifier.create(result)
          .expectNext(6)
          .expectComplete()
          .verify();
    }

    @Test
    void testL3_filter() {
        Flux<Integer> input = Flux.just(5, 10, 15, 20, 25);
        Flux<Integer> result = L4_Transformations.L3_discardFluxItems(input);

        StepVerifier.create(result)
          .expectNext(5, 10)
          .expectComplete()
          .verify();
    }

    @Test
    void testL4_first() {
        Flux<Integer> input = Flux.just(1, 2, 3);
        Mono<Integer> result = L4_Transformations.L4_takeNext(input);

        StepVerifier.create(result)
          .expectNext(1)
          .expectComplete()
          .verify();
    }

    @Test
    void testL5_merge() {
        Flux<Integer> first = Flux.just(1, 2, 3);
        Flux<Integer> second = Flux.just(4, 5, 6);
        Flux<Integer> result = L4_Transformations.L5_combineIntoOne(first, second);

        StepVerifier.create(result)
          .expectNext(1, 2, 3, 4, 5, 6)
          .expectComplete()
          .verify();
    }

    @Test
    void testL6_count() {
        Flux<Integer> input = Flux.just(1, 2, 3, 4, 5);
        Mono<Long> result = L4_Transformations.L6_countAllFluxItems(input);

        StepVerifier.create(result)
          .expectNext(5L)
          .expectComplete()
          .verify();
    }

    @Test
    void testL7_flatten() {
        Flux<List<Integer>> input = Flux.just(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
        Flux<Integer> result = L4_Transformations.L7_flatten(input);

        StepVerifier.create(result)
          .expectNext(1, 2, 3, 4, 5)
          .expectComplete()
          .verify();
    }

    @Test
    void testL8_zip() {
        Flux<String> first = Flux.just("foo", "bar");
        Flux<Integer> second = Flux.just(1, 2);
        Flux<String> result = L4_Transformations.L8_combineTwoFluxes(first, second);

        StepVerifier.create(result)
          .expectNext("foo-1", "bar-2")
          .expectComplete()
          .verify();
    }

    @Test
    void testL9_skip() {
        Flux<Integer> input = Flux.just(1, 2, 3, 4, 5);
        Flux<Integer> result = L4_Transformations.L9_ignoreElements(input, 3);

        StepVerifier.create(result)
          .expectNext(4, 5)
          .expectComplete()
          .verify();
    }

    @Test
    void testL10_take() {
        Flux<Integer> input = Flux.just(1, 2, 3, 4, 5);
        Flux<Integer> result = L4_Transformations.L10_limitFluxSize(input, 3);

        StepVerifier.create(result)
          .expectNext(1, 2, 3)
          .expectComplete()
          .verify();
    }

    @Test
    void testL11_takeUntilEven() {
        Flux<Integer> input = Flux.just(1, 3, 5, 4, 2, 6);
        Flux<Integer> result = L4_Transformations.L11_stopProcessingAfterCondition(input);

        StepVerifier.create(result)
          .expectNext(1, 3, 5)
          .expectComplete()
          .verify();
    }

    @Test
    void testL12_convertSignalsIntoItems() {
        Flux<String> flux = Flux.just("alpha", "bravo", "charlie");

        StepVerifier.create(L12_convertSignalsIntoItems(flux))
          .expectNextMatches(s -> s.isOnNext() && s.get().equals("alpha"))
          .expectNextMatches(s -> s.isOnNext() && s.get().equals("bravo"))
          .expectNextMatches(s -> s.isOnNext() && s.get().equals("charlie"))
          .expectNextMatches(Signal::isOnComplete)
          .verifyComplete();
    }

    @Test
    void testL13_zipWithIndex() {
        Flux<String> flux = Flux.just("alpha", "bravo", "charlie");

        StepVerifier.create(L13_zipWithIndex(flux))
          .expectNext(Tuples.of(0L, "alpha"))
          .expectNext(Tuples.of(1L, "bravo"))
          .expectNext(Tuples.of(2L, "charlie"))
          .verifyComplete();
    }

    @Test
    void testL14_wordCount() {
        Flux<String> words = Flux.just("apple", "banana", "apple", "orange", "banana", "apple");

        StepVerifier.create(L14_wordCount(words))
          .expectNext(Map.of("apple", 3L, "orange", 1L, "banana", 2L))
          .verifyComplete();
    }

    @Test
    void testL15_computePartials() throws Exception {
        StepVerifier.create(L15_extractPartialSums(Flux.range(1, 10)))
          .expectNext(1)
          .expectNext(1 + 2)
          .expectNext(3 + 3)
          .expectNext(6 + 4)
          .expectNext(10 + 5)
          .expectNext(15 + 6)
          .expectNext(21 + 7)
          .expectNext(28 + 8)
          .expectNext(36 + 9)
          .expectNext(45 + 10)
          .verifyComplete();
    }
}
