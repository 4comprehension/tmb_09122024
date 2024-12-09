package com.for_comprehension.reactor.l2_thread_pool;

import com.pivovarit.collectors.ParallelCollectors;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class L2_CPU_vs_IO_Bound {

    record CPU_Bound() {
        public static void main(String[] args) {
            String[] strings = Stream.generate(() -> UUID.randomUUID().toString())
              .limit(10_000_000)
              .toArray(String[]::new);

            System.out.println("ForkJoinPool.getCommonPoolParallelism() = " + ForkJoinPool.getCommonPoolParallelism());
            System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime().availableProcessors());
            // cpu-bound

            // experiment with parallelism level from 2 beyond Runtime.getRuntime().availableProcessors()
            // -Djava.util.concurrent.ForkJoinPool.common.parallelism=12
            timed(() -> Arrays.parallelSort(strings));
        }
    }

    record IO_Bound() {
        public static void main(String[] args) {
            try (var e = Executors.newCachedThreadPool()) {
                timed(() -> {
                    Stream.iterate(0, i -> i + 1)
                      .limit(1000)
                      .collect(ParallelCollectors.parallel(i -> process(i), Collectors.toList(), e, 1000))
                      .join();
                });
            }
        }
    }

    static void timed(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) + " ms");
    }

    static <T> T process(T input) {
        try {
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
