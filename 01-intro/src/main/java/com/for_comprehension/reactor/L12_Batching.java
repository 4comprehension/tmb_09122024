package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

class L12_Batching {

    /**
     * Buffer the incoming {@link Flux} into lists of N items and then emit these lists
     */
    static Flux<List<Integer>> L1_buffer(Flux<Integer> numbers, int n) {
        return numbers.buffer(n);
    }

    /**
     * Buffer the incoming {@link Flux} into lists of max N items... but do not wait more than provided timeout
     */
    static Flux<List<Integer>> L2_bufferOrTimeout(Flux<Integer> numbers, int n, Duration timeout) {
        return numbers.bufferTimeout(n, timeout);
    }

    /**
     * Create a sliding window that buffers items from the incoming {@link Flux}.
     * Each buffered list contains N items and starts a new buffer window every M items.
     * For example, if maxSize is 5 and skip is 3:
     * Input: 1, 2, 3, 4, 5, 6, 7, 8, 9
     * Output: [1, 2, 3, 4, 5], [4, 5, 6, 7, 8], [7, 8, 9]
     * <p>
     * Note how values overlap
     *
     * Bonus: buffer into LinkedLists
     */
    static Flux<List<Integer>> L3_overlappingBuffer(Flux<Integer> numbers, int maxSize, int skip) {
        return numbers.window(maxSize, skip).flatMap(Flux::collectList);
    }
}
