package com.for_comprehension.reactor.l1_thread;

class L1_DaemonThread {
    record Example1() {
        public static void main(String[] args) throws InterruptedException {
            Thread.sleep(1000);
        }
    }

    record Example2() {
        public static void main(String[] args) throws InterruptedException {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    record Example3() {
        public static void main(String[] args) throws InterruptedException {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }
}
