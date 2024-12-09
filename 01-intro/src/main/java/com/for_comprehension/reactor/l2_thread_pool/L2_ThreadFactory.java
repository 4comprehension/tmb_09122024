package com.for_comprehension.reactor.l2_thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

class L2_ThreadFactory {

    public static void main(String[] args) {
        ExecutorService e1 = Executors.newFixedThreadPool(100, named("mail-scheduler"));

        for (int i = 0; i < 100; i++) {
            e1.submit(() -> {});
        }
    }

    // before JDK21
    private static ThreadFactory named(String prefix) {
        return new ThreadFactory() {
            private final AtomicLong seq = new AtomicLong();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, prefix + "-" + seq.getAndIncrement());
            }
        };
    }

    // after JDK21
    private static ThreadFactory named2(String prefix) {
        return Thread.ofPlatform()
          .name(prefix + "-", 0)
          .factory();
    }
}
