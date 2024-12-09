package com.for_comprehension.reactor.l1_thread;

class L1_ThreadInterruptHandling {

    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
