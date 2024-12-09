package com.for_comprehension.reactor.l1_thread;

import java.util.concurrent.atomic.AtomicBoolean;

class L1_ThreadInterruptExample {

    public static void main(String[] args) throws InterruptedException {

        Holder holder = new Holder();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000_000; i++) {
                try {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("interrupted, closing...");
                        break;
                    }
                    holder.process(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("interrupted via exception, closing...");
                    break;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000_000; i++) {
                try {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("interrupted, closing...");
                        break;
                    }
                    holder.process(i);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("interrupted via exception, closing...");
                    break;
                }
            }
        });

        t1.start();
        t2.start();

        Thread.sleep(5000);

        t1.interrupt();

        Thread.sleep(3000);

        t2.interrupt();
    }

    static class Holder {
        private final AtomicBoolean dirty = new AtomicBoolean(false);

        public synchronized void process(Integer i) {
            if (dirty.getAndSet(true)) {
                throw new IllegalStateException("can't happen!");
            }

            System.out.println("processing " + i + " on " + Thread.currentThread().getName());
            doSomeWork();

            dirty.set(false);
        }

        private static void doSomeWork() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
