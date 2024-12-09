package com.for_comprehension.reactor.l2_thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class L2_CachedThreadPool {

    record Example1() {
        public static void main(String[] args) {
            int nThreads = 100;
            timed(() -> {
                try (ExecutorService e = Executors.newCachedThreadPool()) {
                    for (int i = 0; i < nThreads; i++) {
                        int finalI = i;
                        e.submit(() -> {
                            process(finalI);
                        });
                    }
                }
            });
        }
    }

    record Example2() {
        public static void main(String[] args) {
            int nThreads = 10000;
            timed(() -> {
                try (ExecutorService e = new ThreadPoolExecutor(0, 1000,
                  60L, TimeUnit.SECONDS,
                  new SynchronousQueue<>())) {
                    for (int i = 0; i < nThreads; i++) {
                        int finalI = i;
                        e.submit(() -> {
                            process(finalI);
                        });
                    }
                }
            });
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
