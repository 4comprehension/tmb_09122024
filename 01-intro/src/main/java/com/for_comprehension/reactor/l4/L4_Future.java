package com.for_comprehension.reactor.l4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class L4_Future {

    public static void main(String[] args) {
        ExecutorService e = Executors.newFixedThreadPool(4);

        Future<Integer> future = e.submit(() -> process(42));
        System.out.println("doing something else...");

        try {
            Integer result = future.get();
            System.out.println("result = " + result);
        } catch (InterruptedException ex) {
            System.out.println("ok, not a big deal, exiting");
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    static <T> T process(T input) {
        try {
            if (true) {
                throw new RuntimeException("oops");
            }
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
