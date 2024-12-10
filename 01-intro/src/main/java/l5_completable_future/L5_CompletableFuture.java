package l5_completable_future;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;

class L5_CompletableFuture {
    record Example1() {
        public static void main(String[] args) {
            var cf1 = new CompletableFuture<Integer>();

            Thread.ofPlatform().start(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                cf1.complete(1);
            });

            Thread.ofPlatform().start(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                cf1.complete(2);
            });

            cf1.thenAccept(System.out::println);
            cf1.handle((result, ex) -> {
                if (ex != null) {
                    System.out.println("exception = " + ex);
                }
                return result;
            });
        }

        record Example2() {
            public static void main(String[] args) {
                ExecutorService e = Executors.newFixedThreadPool(1);
                // always remember to provide an executor because the default one is ForkJoinPool.commonPool()
                CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
                    return process(42);
                }, e);

                System.out.println("cf1.join() = " + cf1.join());
            }
        }

        record PostCompleteDebugging() {
            public static void main(String[] args) {
                var cf1 = new CompletableFuture<Integer>();
                cf1.thenAccept(System.out::println);
                cf1.complete(42);
            }
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
