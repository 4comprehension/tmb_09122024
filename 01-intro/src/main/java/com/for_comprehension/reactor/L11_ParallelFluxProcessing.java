package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

class L11_ParallelFluxProcessing {

    /**
     * Process the Flux in parallel using the provided mapping function
     */
    static <T, R> Mono<Set<R>> L1_processParallel(Flux<T> flux, Function<T, Mono<R>> mapping, Executor executor) {
        return flux
          .flatMap(i -> mapping.apply(i)
            .subscribeOn(Schedulers.fromExecutor(executor)))
          .collect(Collectors.toSet());
    }

    /**
     * Process the sequence in parallel (maintain ordering) and collect to list, max parallelism: 2
     */
    static <T, R> Mono<List<R>> L2_processParallelOrdered(Flux<T> flux, Function<T, Mono<R>> mapping, Executor executor) {
        return flux
          .flatMapSequential(i -> mapping.apply(i)
            .subscribeOn(Schedulers.fromExecutor(executor)), 2)
          .collectList();
    }

    /**
     * Process the sequence in parallel and collect results produced by each thread into separate lists, parallelism 3
     */
    static <T, R> Flux<List<R>> L3_processInGroups(Flux<T> flux, Function<T, Mono<R>> mapping, Executor executor) {
        return flux
          .parallel(3)
          .runOn(Schedulers.fromExecutor(executor))
          .flatMap(mapping)
          .groups()
          .flatMap(Flux::collectList)
          .log();
    }

    /**
     * Process the sequence in parallel and sum all the results
     */
    static Mono<Long> L4_flatMapReduce(Flux<Integer> flux, Function<Integer, Mono<Integer>> mapping, Executor executor) {
        return flux
          .flatMapSequential(i -> mapping.apply(i)
            .subscribeOn(Schedulers.fromExecutor(executor)), 2)
          .reduce(0L, Long::sum);
    }

    /**
     * Process the sequence in parallel on executor 1 with max parallelism 3 and then on executor 2 with max parallelism 2
     */
    static <T, R, RR> Mono<List<RR>> L5_processOnTwoExecutors(Flux<T> flux, Function<T, Mono<R>> f1, Function<R, Mono<RR>> f2, Executor e1, Executor e2) {
        return flux
          .flatMap(i -> f1.apply(i)
            .subscribeOn(Schedulers.fromExecutor(e1)), 3)
          .flatMap(i -> f2.apply(i)
            .subscribeOn(Schedulers.fromExecutor(e2)), 2)
          .collectList();
    }

    /**
     * Process the sequence in parallel on executor 1 with max parallelism 4 and then on executor 2 with max parallelism 2
     * <p>
     * Ensure that this flux is processed in two stages:
     * - stage 1: all f1 operations
     * - stage 2: all f2 operations
     */
    static <T, R, RR> Mono<List<RR>> L6_processStaged(Flux<T> flux, Function<T, Mono<R>> f1, Function<R, Mono<RR>> f2, Executor e1, Executor e2) {
        return flux
          .flatMapSequential(i -> f1.apply(i)
            .subscribeOn(Schedulers.fromExecutor(e1)), 4)
          .collectList()
          .flux()
          .flatMapIterable(i -> i)
          .flatMapSequential(i -> f2.apply(i)
            .subscribeOn(Schedulers.fromExecutor(e2)), 2)
          .collectList();
    }
}
