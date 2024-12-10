package com.for_comprehension.reactor.l4_future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class L4_FutureTask {

    public static void main(String[] args) {
        try (var e = Executors.newFixedThreadPool(4)) {
            Future<Integer> submit = e.submit(() -> process(42));

            Thread.sleep(1000);
            submit.cancel(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T process(T input) {
        try {
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("interrupted, stopping");
        }
        return input;
    }
}
