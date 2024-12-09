package com.for_comprehension.reactor.l1_thread;

import java.util.concurrent.atomic.AtomicBoolean;

// runs on Java 17
class L1_ThreadStopExample {

    public static void main(String[] args) throws InterruptedException {

        Holder holder = new Holder();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000_000; i++) {
                try {
                    holder.process(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000_000; i++) {
                try {
                    holder.process(i);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
        });

        t1.start();
        t2.start();

        Thread.sleep(5000);

        t1.stop();
    }

    static class Holder {
        private final AtomicBoolean dirty = new AtomicBoolean(false);

        public synchronized void process(Integer i) {
            if (dirty.getAndSet(true)) {
                throw new IllegalStateException("can't happen!");
            }

            System.out.println("processing " + i + " on " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            dirty.set(false);
        }

    }
}
