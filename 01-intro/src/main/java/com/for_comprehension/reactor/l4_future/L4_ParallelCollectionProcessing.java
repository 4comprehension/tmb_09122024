package com.for_comprehension.reactor.l4_future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

class L4_ParallelCollectionProcessing {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> ints = Stream.iterate(0, i -> i + 1)
          .limit(100)
          .toList();

        // process in parallel with max parallelism of 15
        timed(() -> {
            ExecutorService e = Executors.newFixedThreadPool(15);
            List<Future<Integer>> tasks = new ArrayList<>();
            for (Integer i : ints) {
                Future<Integer> future = e.submit(() -> process(i));
                tasks.add(future);
            }

            List<Integer> results = tasks.stream()
              .map(f -> {
                  try {
                      return f.get();
                  } catch (Exception ex) {
                      throw new RuntimeException(ex);
                  }
              }).toList();

            System.out.println("results = " + results);
        });
    }

    record ParallelStreamsWithOutParallelStream() {
        public static void main(String[] args) throws InterruptedException {
            List<Integer> ints = Stream.iterate(0, i -> i + 1)
              .limit(100)
              .toList();

            // process in parallel with max parallelism of 15
            timed(() -> {
                ExecutorService e = Executors.newFixedThreadPool(15);
                List<Integer> results = ints.stream()
                  .map(i -> e.submit(() -> process(i)))
                  .toList()
                  .stream()
                  .map(f -> {
                      try {
                          return f.get();
                      } catch (Exception ex) {
                          throw new RuntimeException(ex);
                      }
                  }).toList();

                System.out.println("results = " + results);
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

    record Example() {
        public static void main(String[] args) {
            Stream.of(1,2,3,4)
              .map(i -> i)
              .map(i -> i)
              .map(i -> i)
              .map(i -> {
                  System.out.println(i);
                  return i;
              })
              .findFirst();

        }
    }
}
