package com.for_comprehension.reactor.l1_thread;

class L1_Thread {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("Hello from " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        });

        thread.start();
        Thread.sleep(5000);
        thread.stop();
    }


}
