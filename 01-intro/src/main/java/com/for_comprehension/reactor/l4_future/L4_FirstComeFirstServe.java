package com.for_comprehension.reactor.l4_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class L4_FirstComeFirstServe {

    record BusyWaiting() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService e = Executors.newCachedThreadPool();

            Future<Integer> f1 = e.submit(() -> process(1));
            Future<Integer> f2 = e.submit(() -> process(2));

            while (true) {
                if (f1.isDone()) {
                    try {
                        var result = f1.get();
                        System.out.println("result = " + result);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                } else if (f2.isDone()) {
                    try {
                        var result = f2.get();
                        System.out.println("result = " + result);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }

                Thread.sleep(100);
                System.out.println("waiting...");
            }

            e.shutdown();

            // wait for the first task to complete and print it
        }
    }

    record Queue() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService e = Executors.newCachedThreadPool();

            LinkedBlockingQueue<Integer> results = new LinkedBlockingQueue<>();
            e.submit(() -> {
                try {
                    results.put(process(1));
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            e.submit(() -> {
                try {
                    results.put(process(2));
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });

            System.out.println("results.take() = " + results.take());
        }
    }

    record CF() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService e = Executors.newCachedThreadPool();

            CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> process(1), e);
            CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> process(2), e);

            Integer result = cf1.applyToEither(cf2, i -> i).join();
            System.out.println("result = " + result);
        }
    }

    static <T> T process(T input) {
        try {
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(ThreadLocalRandom.current().nextInt(5_000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
