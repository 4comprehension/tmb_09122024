package com.for_comprehension.reactor.l3_parallel_stream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

class L3_ParallelStreamsPollution {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime()
          .availableProcessors());

        Thread.ofPlatform().start(() -> {
            Stream.generate(() -> 42)
              .parallel()
              .limit(Runtime.getRuntime()
                .availableProcessors())

              .forEach(_ -> {
                  try {
                      Thread.sleep(Integer.MAX_VALUE);
                  } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                  }
              });
        });

        Thread.sleep(500);

        timed(() -> {
            Stream.iterate(0, i -> i + 1)
              .limit(20)
              .parallel()
              .map(i -> process(i))
              .forEach(_ -> {});
        });
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
