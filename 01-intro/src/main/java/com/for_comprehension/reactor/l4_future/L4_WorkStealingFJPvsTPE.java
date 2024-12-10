package com.for_comprehension.reactor.l4_future;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

class L4_WorkStealingFJPvsTPE {

    record WorkStealingWithFJP() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService forkJoinPool = Executors.newWorkStealingPool(2);

            // t1: [1s, 1s, 1s, 1s ]
            // t2: [10s, 1s, 1s, 1s ]

            forkJoinPool.submit(() -> process(1, Duration.ofSeconds(1)));
            forkJoinPool.submit(() -> process(2, Duration.ofSeconds(10)));
            forkJoinPool.submit(() -> process(3, Duration.ofSeconds(1)));
            forkJoinPool.submit(() -> process(4, Duration.ofSeconds(1)));
            forkJoinPool.submit(() -> process(5, Duration.ofSeconds(1)));
            forkJoinPool.submit(() -> process(6, Duration.ofSeconds(1)));
            forkJoinPool.submit(() -> process(7, Duration.ofSeconds(1)));
            forkJoinPool.submit(() -> process(8, Duration.ofSeconds(1)));


            Thread.currentThread().join();
        }
    }

    record WorkStealingWithThreadPoolExecutor() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService tpool = Executors.newFixedThreadPool(2);

            // t1, t2: [1s, 1s, 1s, 1s, 10s, 1s, 1s, 1s ]

            tpool.submit(() -> process(1, Duration.ofSeconds(1)));
            tpool.submit(() -> process(3, Duration.ofSeconds(1)));
            tpool.submit(() -> process(4, Duration.ofSeconds(1)));
            tpool.submit(() -> process(5, Duration.ofSeconds(1)));
            tpool.submit(() -> process(6, Duration.ofSeconds(1)));
            tpool.submit(() -> process(7, Duration.ofSeconds(1)));
            tpool.submit(() -> process(8, Duration.ofSeconds(1)));
            tpool.submit(() -> process(2, Duration.ofSeconds(10)));


            Thread.currentThread().join();
        }
    }

    static <T> T process(T input, Duration sleep) {
        try {
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(sleep.toMillis());
        } catch (InterruptedException e) {
        }
        return input;
    }
}
