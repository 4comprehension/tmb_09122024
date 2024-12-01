package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class L5_StreamAPIInterop {

    /**
     * Convert a given {@link Flux} instance into {@link Stream} instance
     */
    static Stream<Integer> L0_convertToStream(Flux<Integer> integers) {
        return integers.toStream();
    }

    /**
     * Collect a given {@link Flux} instance into an immutable {@link List}
     *
     * @apiNote try to NOT convert Flux to Stream first :)
     */
    static Mono<List<Integer>> L1_collectFluxIntoImmutableList(Flux<Integer> integers) {
        return integers.collect(Collectors.toUnmodifiableList());
    }
}
