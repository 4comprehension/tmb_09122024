package com.for_comprehension.reactor.l5_completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Function;

class L5_CompletableFutureThreading {
    record Example1() {
        public static void main(String[] args) {
            try (var e = Executors.newFixedThreadPool(4)) {
                CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> process(42), e);

                System.out.println("waiting for the operation to complete...");
                Thread.sleep(5_000);
                System.out.println("operation completed, adding more operations...");

                integerCompletableFuture
                  .thenApply(log())
                  .thenApply(log())
                  .thenApply(log());

                System.out.println("done!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private static Function<Integer, Integer> log() {
            return i -> {
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }

        private static Function<Integer, Integer> slowLog() {
            return i -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }
    }

    record Example2() {
        public static void main(String[] args) {
            try (var e = Executors.newFixedThreadPool(4)) {
                CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> process(42), e);

                System.out.println("waiting for the operation to complete...");
                Thread.sleep(5_000);
                System.out.println("operation completed, adding more operations...");

                integerCompletableFuture
                  .thenApply(slowLog())
                  .thenApply(slowLog())
                  .thenApply(slowLog());

                System.out.println("done!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private static Function<Integer, Integer> log() {
            return i -> {
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }

        private static Function<Integer, Integer> slowLog() {
            return i -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }
    }

    record Example3() {
        public static void main(String[] args) {
            try (var e = Executors.newFixedThreadPool(4)) {
                CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> process(42), e);

                System.out.println("waiting for the operation to complete...");
                Thread.sleep(5_000);
                System.out.println("operation completed, adding more operations...");

                integerCompletableFuture
                  .thenApplyAsync(slowLog())
                  .thenApplyAsync(slowLog())
                  .thenApplyAsync(slowLog())
                  .join();

                System.out.println("done!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private static Function<Integer, Integer> log() {
            return i -> {
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }

        private static Function<Integer, Integer> slowLog() {
            return i -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }
    }

    record Example4() {
        public static void main(String[] args) {
            try (var e = Executors.newFixedThreadPool(4)) {
                CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> process(42), e);

                System.out.println("waiting for the operation to complete...");
                Thread.sleep(5_000);
                System.out.println("operation completed, adding more operations...");

                integerCompletableFuture
                  .thenApplyAsync(slowLog(), e)
                  .thenApplyAsync(slowLog(), e)
                  .thenApplyAsync(slowLog(), e)
                  .join();

                System.out.println("done!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private static Function<Integer, Integer> log() {
            return i -> {
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }

        private static Function<Integer, Integer> slowLog() {
            return i -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thenApply on " + Thread.currentThread().getName());
                return i;
            };
        }
    }

    static <T> T process(T input) {
        try {
            System.out.println("processing input = " + input + " on " + Thread.currentThread().getName());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("interrupted, stopping");
        }
        return input;
    }
}
