package com.for_comprehension.reactor.l4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

class L4_FirstComeFirstServe {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService e = Executors.newCachedThreadPool();

        Future<Integer> f1 = e.submit(() -> process(1));
        Future<Integer> f2 = e.submit(() -> process(2));

        // wait for the first task to complete and print it
    }

    static <T> T process(T input) {
        try {
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(ThreadLocalRandom.current().nextInt(10_000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
