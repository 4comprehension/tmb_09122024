package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;

class L4_Transformations {

    /**
     * Increment each element in the given Flux of Integers by 1
     * Bonus question: what thread is it executed on?
     */
    static Flux<Integer> L0_incrementAll(Flux<Integer> integers) {
        return integers.map(i -> i + 1);
    }

    /**
     * Convert each incoming element into a Integer
     */
    static Flux<Integer> L1_castToInteger(Flux<Object> integers) {
        return integers.cast(Integer.class);
    }

    /**
     * Convert a {@link Flux} of integers into a {@link Mono} containing its sum
     */
    static Mono<Integer> L2_sumAll(Flux<Integer> integers) {
        return integers.reduce(0, Integer::sum);
    }

    /**
     * Filter out all the numbers greater than 10 in the incoming {@link Flux}
     */
    static Flux<Integer> L3_discardFluxItems(Flux<Integer> numbers) {
        return numbers.filter(number -> number <= 10);
    }

    /**
     * Return a Mono that represents the first element in the incoming {@link Flux}
     */
    static Mono<Integer> L4_takeNext(Flux<Integer> numbers) {
        return numbers.next();
    }

    /**
     * Merge the two incoming {@link Flux}s into one ([1,2] + [3, 4] -> [1, 2, 3, 4])
     *
     * @implNote it's not about zipping, a solution in similar to combining two Java Streams together
     */
    static Flux<Integer> L5_combineIntoOne(Flux<Integer> first, Flux<Integer> second) {
        return Flux.concat(first, second);
    }

    /**
     * Transform the incoming {@link Flux} to a Mono that represents the number of elements in the {@link Flux}
     */
    static Mono<Long> L6_countAllFluxItems(Flux<Integer> numbers) {
        return numbers.count();
    }

    /**
     * Flatten the incoming {@link Flux} of Lists of Integers to a {@link Flux} of Integers
     */
    static Flux<Integer> L7_flatten(Flux<List<Integer>> flux) {
        return flux.flatMapIterable(i -> i);
    }

    /**
     * Merge two {@link Flux}s into a single one by taking an element from each, and creating a "-" separated String
     * <p>
     * For example, if the first one returns ["foo", "bar"] and the second [1, 2], the output should return ["foo-1", "bar-2"]
     */
    static Flux<String> L8_combineTwoFluxes(Flux<String> first, Flux<Integer> second) {
        return first.zipWith(second, "%s-%d"::formatted);
    }

    /**
     * Suppress the first N elements in the incoming {@link Flux}
     */
    static Flux<Integer> L9_ignoreElements(Flux<Integer> numbers, int n) {
        return numbers.skip(n);
    }

    /**
     * Take only the first N elements in the incoming {@link Flux}
     */
    static Flux<Integer> L10_limitFluxSize(Flux<Integer> numbers, int n) {
        return numbers.take(n);
    }

    /**
     * Take elements from the incoming {@link Flux} until an even number is encountered.
     */
    static Flux<Integer> L11_stopProcessingAfterCondition(Flux<Integer> numbers) {
        return numbers.takeWhile(num -> num % 2 != 0);
    }

    /**
     * As you know, there are not just items flowing through a flux, but varius signals... let's materialize them
     *
     * @apiNote try to figure out how to reverse it
     */
    static <T> Flux<Signal<T>> L12_convertSignalsIntoItems(Flux<T> flux) {

        return flux.materialize();
    }

    /**
     * As you know, there are not just items flowing through a flux, but various signals... let's materialize them
     *
     * @apiNote try to figure out how to reverse it
     */
    static <T> Flux<Tuple2<Long, T>> L13_zipWithIndex(Flux<T> flux) {
        return flux.index();
    }

    /**
     * Get number of occurrences of each word
     */
    static Mono<Map<String, Long>> L14_wordCount(Flux<String> words) {
        return words.groupBy(s -> s)
          .flatMap(w -> w.count().map(c -> Map.entry(w.key(), c)))
          .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    /**
     * Get number of occurrences of each word
     */
    static Flux<Integer> L15_extractPartialSums(Flux<Integer> integers) {
        return integers.scan((acc, num) -> acc + num);
    }



}
