package com.for_comprehension.reactor.l4;

import java.util.List;
import java.util.stream.Stream;

class L4_ParallelCollectionProcessing {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> ints = Stream.iterate(0, i -> i + 1)
          .limit(100)
          .toList();

        timed(() -> {
            // TODO

            List<Integer> results = null;
            System.out.println("results = " + results);
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
