package com.for_comprehension.reactor.l2_thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class L2_ThreadPoolConfiguration {

    record Example1() {
        public static void main(String[] args) {
            timed(() -> {
                int nThreads = 4;
                try (ExecutorService e = Executors.newFixedThreadPool(nThreads)) {
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
            timed(() -> {
                int nThreads = 4;
                try (ExecutorService e = Executors.newFixedThreadPool(nThreads)) {
                    for (int i = 0; i < nThreads + 1; i++) {
                        int finalI = i;
                        e.submit(() -> {
                            process(finalI);
                        });
                    }
                }
            });
        }
    }

    record Example3() {
        public static void main(String[] args) {
            int nThreads = 4;
            int qSize = 10;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, nThreads,
                  0L, TimeUnit.MILLISECONDS,
                  new LinkedBlockingQueue<>(qSize));
                for (int i = 0; i < nThreads + qSize + 1; i++) {
                    int finalI = i;
                    e.submit(() -> {
                        process(finalI);
                    });
                }

                e.shutdown();
                try {
                    e.awaitTermination(1, TimeUnit.DAYS);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    record Example4() {
        public static void main(String[] args) {
            int nThreads = 4;
            int maxThreads = 8;
            int qSize = 10;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, maxThreads,
                  60, TimeUnit.SECONDS,
                  new LinkedBlockingQueue<>(qSize));
                for (int i = 0; i < nThreads + qSize + 1; i++) {
                    int finalI = i;
                    e.submit(() -> {
                        process(finalI);
                    });
                }

//                e.shutdown();
                try {
                    e.awaitTermination(1, TimeUnit.DAYS);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    record Example5() {
        public static void main(String[] args) {
            int nThreads = 4;
            int maxThreads = 8;
            int qSize = 10;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, maxThreads,
                  60, TimeUnit.SECONDS,
                  new LinkedBlockingQueue<>(qSize));
                for (int i = 0; i < maxThreads + qSize + 1; i++) {
                    int finalI = i;
                    e.submit(() -> {
                        process(finalI);
                    });
                }

//                e.shutdown();
                try {
                    e.awaitTermination(1, TimeUnit.DAYS);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    record Example6() {
        public static void main(String[] args) {
            int nThreads = 4;
            int maxThreads = 8;
            int qSize = 10;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, maxThreads,
                  60, TimeUnit.SECONDS,
                  new LinkedBlockingQueue<>(qSize), new ThreadPoolExecutor.AbortPolicy());
                for (int i = 0; i < maxThreads + qSize + 1; i++) {
                    int finalI = i;
                    e.submit(() -> {
                        process(finalI);
                    });
                }

//                e.shutdown();
                try {
                    e.awaitTermination(1, TimeUnit.DAYS);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
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
