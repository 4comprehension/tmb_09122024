package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

class L5_BufferingThrottling {

    public static void main(String[] args) {
    }

    /**
     * Limit the rate of emissions in the incoming {@link Flux}
     *
     * @implNote this is super tricky to test, use {@link Flux#log()} and observe differences
     */
    static void L1_throttle() {
    }

    /**
     * Delay the emission of each item in the incoming {@link Flux} by 1 second
     */
    static Flux<Integer> L2_delay(Flux<Integer> numbers) {
        return numbers.delayElements(Duration.ofSeconds(1));
    }

    /**
     * Buffer the incoming {@link Flux} into lists of N items and then emit these lists
     */
    static Flux<List<Integer>> L3_buffer(Flux<Integer> numbers, int n) {
        return numbers.buffer(n);
    }

    /**
     * Emit items from the incoming {@link Flux} at fixed rate of one item every 800 ms, dropping the others
     */
    static Flux<Integer> L5_sample(Flux<Integer> numbers) {
        return numbers.sample(Duration.ofMillis(800));
    }

    /**
     * Delay subscription to the source Flux by a given duration
     */
    static Flux<Integer> L6_delaySubscription(Flux<Integer> numbers, Duration delay) {
        return numbers.delaySubscription(delay);
    }

    /**
     * Create a sliding window that buffers items from the incoming {@link Flux}.
     * Each buffered list contains N items and starts a new buffer window every M items.
     * For example, if maxSize is 5 and skip is 3:
     * Input: 1, 2, 3, 4, 5, 6, 7, 8, 9
     * Output: [1, 2, 3, 4, 5], [4, 5, 6, 7, 8], [7, 8, 9]
     * <p>
     * Note how values overlap
     */
    static Flux<List<Integer>> L7_slidingWindowBuffer(Flux<Integer> numbers, int maxSize, int skip) {
        return numbers.buffer(maxSize, skip);
    }
}
