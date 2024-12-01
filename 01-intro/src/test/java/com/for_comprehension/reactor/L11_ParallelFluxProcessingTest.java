package com.for_comprehension.reactor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.tools.agent.ReactorDebugAgent;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class L11_ParallelFluxProcessingTest {

    @Test
    void l1_processParallel() {
        CountingExecutorService e = new CountingExecutorService(Executors.newCachedThreadPool());

        Flux<Integer> source = Flux.just(1, 2, 3, 4);
        Function<Integer, Mono<Integer>> doubleFn = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        StepVerifier.create(L11_ParallelFluxProcessing.L1_processParallel(source, doubleFn, e).log())
          .thenAwait(Duration.ofSeconds(3))
          .expectNextMatches(s -> s.containsAll(List.of(2, 4, 6, 8)))
          .expectComplete()
          .verifyThenAssertThat()
          .tookLessThan(Duration.ofMillis(2400));

        assertThat(e.getCount()).isEqualTo(4);
    }

    @Test
    void l2_processParallelOrdered() {
        CountingExecutorService e = new CountingExecutorService(Executors.newCachedThreadPool());

        Flux<Integer> source = Flux.just(1, 2, 3, 4);
        Function<Integer, Mono<Integer>> doubleFn = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        StepVerifier.create(L11_ParallelFluxProcessing.L2_processParallelOrdered(source, doubleFn, e).log())
          .expectNext(List.of(2, 4, 6, 8))
          .expectComplete()
          .verifyThenAssertThat()
          .tookLessThan(Duration.ofMillis(4200));

        assertThat(e.getCount()).isEqualTo(4);
    }

    @Test
    void l3_processInGroups() {
        CountingExecutorService e = new CountingExecutorService(Executors.newCachedThreadPool());

        Flux<Integer> source = Flux.just(1, 2, 3, 4);
        Function<Integer, Mono<Integer>> doubleFn = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        StepVerifier.create(L11_ParallelFluxProcessing.L3_processInGroups(source, doubleFn, e).log())
          .expectNextCount(3)
          .expectComplete()
          .verifyThenAssertThat()
          .tookLessThan(Duration.ofMillis(4200));
    }

    @Test
    void l4_flatMapReduce() {
        CountingExecutorService e = new CountingExecutorService(Executors.newCachedThreadPool());

        Flux<Integer> source = Flux.just(1, 2, 3, 4);
        Function<Integer, Mono<Integer>> doubleFn = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        StepVerifier.create(L11_ParallelFluxProcessing.L4_flatMapReduce(source, doubleFn, e).log())
          .expectNext(20L)
          .expectComplete()
          .verifyThenAssertThat()
          .tookLessThan(Duration.ofMillis(4200));

        Assertions.assertThat(e.getCount()).isEqualTo(4);
    }

    @Test
    void l5_processOnTwoExecutors() {
        CountingExecutorService e1 = new CountingExecutorService(Executors.newCachedThreadPool());
        CountingExecutorService e2 = new CountingExecutorService(Executors.newCachedThreadPool());

        Flux<Integer> source = Flux.just(1, 2, 3, 4);
        Function<Integer, Mono<Integer>> doubleFn1 = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing1 on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        Function<Integer, Mono<Integer>> doubleFn2 = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing2 on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        StepVerifier.create(L11_ParallelFluxProcessing.L5_processOnTwoExecutors(source, doubleFn1, doubleFn2, e1, e2)
            .log())
          .expectNextMatches(l -> l.containsAll(List.of(4, 8, 12, 16)))
          .expectComplete()
          .verifyThenAssertThat()
          .tookLessThan(Duration.ofMillis(6200));

        Assertions.assertThat(e1.getCount()).isEqualTo(4);
        Assertions.assertThat(e2.getCount()).isEqualTo(4);
    }

    @Test
    void l6_processStaged() {
        CountingExecutorService e1 = new CountingExecutorService(Executors.newCachedThreadPool());
        CountingExecutorService e2 = new CountingExecutorService(Executors.newCachedThreadPool());

        Flux<Integer> source = Flux.just(1, 2, 3, 4);
        Function<Integer, Mono<Integer>> doubleFn1 = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing1 on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        Function<Integer, Mono<Integer>> doubleFn2 = i -> Mono.fromCallable(() -> {
            try {
                System.out.println("processing2 on: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return i * 2;
        });

        StepVerifier.create(L11_ParallelFluxProcessing.L6_processStaged(source, doubleFn1, doubleFn2, e1, e2)
            .log())
          .expectNextMatches(l -> l.containsAll(List.of(4, 8, 12, 16)))
          .expectComplete()
          .verifyThenAssertThat()
          .tookLessThan(Duration.ofMillis(6200));

        Assertions.assertThat(e1.getCount()).isEqualTo(4);
        Assertions.assertThat(e2.getCount()).isEqualTo(4);
    }

    private static class CountingExecutorService implements ExecutorService {

        private final ExecutorService delegate;
        private final AtomicInteger counter;

        public CountingExecutorService(ExecutorService executor, AtomicInteger counter) {
            this.delegate = executor;
            this.counter = counter;
        }

        CountingExecutorService(ExecutorService delegate) {
            this.delegate = delegate;
            this.counter = new AtomicInteger();
        }

        public Integer getCount() {
            return counter.get();
        }

        @Override
        public void shutdown() {
            delegate.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            return delegate.shutdownNow();
        }

        @Override
        public boolean isShutdown() {
            return delegate.isShutdown();
        }

        @Override
        public boolean isTerminated() {
            return delegate.isTerminated();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.awaitTermination(timeout, unit);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            counter.incrementAndGet();
            return delegate.submit(task);
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            counter.incrementAndGet();
            return delegate.submit(task, result);
        }

        @Override
        public Future<?> submit(Runnable task) {
            counter.incrementAndGet();
            return delegate.submit(task);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            counter.incrementAndGet();
            return delegate.invokeAll(tasks);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            counter.incrementAndGet();
            return delegate.invokeAll(tasks, timeout, unit);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
            counter.incrementAndGet();
            return delegate.invokeAny(tasks);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            counter.incrementAndGet();
            return delegate.invokeAny(tasks, timeout, unit);
        }

        @Override
        public void execute(Runnable command) {
            counter.incrementAndGet();
            delegate.execute(command);
        }
    }
}
