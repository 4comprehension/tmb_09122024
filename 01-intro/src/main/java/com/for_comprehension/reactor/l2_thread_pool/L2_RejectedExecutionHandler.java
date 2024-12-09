package com.for_comprehension.reactor.l2_thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class L2_RejectedExecutionHandler {

    record Example_Abort() {
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

    record Example_Discard() {
        public static void main(String[] args) {
            int nThreads = 2;
            int maxThreads = 2;
            int qSize = 2;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, maxThreads,
                  60, TimeUnit.SECONDS,
                  new LinkedBlockingQueue<>(qSize), new ThreadPoolExecutor.DiscardPolicy());
                for (int i = 0; i < maxThreads + qSize + 1; i++) {
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

    record Example_DiscardOldest() {
        public static void main(String[] args) {
            int nThreads = 2;
            int maxThreads = 2;
            int qSize = 2;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, maxThreads,
                  60, TimeUnit.SECONDS,
                  new LinkedBlockingQueue<>(qSize), new ThreadPoolExecutor.DiscardOldestPolicy());
                for (int i = 0; i < maxThreads + qSize + 1; i++) {
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

    record Example_CallerRunsPolicy() {
        public static void main(String[] args) {
            int nThreads = 4;
            int maxThreads = 8;
            int qSize = 10;
            timed(() -> {
                ExecutorService e = new ThreadPoolExecutor(nThreads, maxThreads,
                  60, TimeUnit.SECONDS,
                  new LinkedBlockingQueue<>(qSize),
                  // instead of waiting passively... do the work yourself!
                  new ThreadPoolExecutor.CallerRunsPolicy()
/*                  new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("got an exception... retrying in 2 secs!");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }

                        executor.submit(r);
                    }
                }*/);
                for (int i = 0; i < maxThreads + qSize + 3; i++) {
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
