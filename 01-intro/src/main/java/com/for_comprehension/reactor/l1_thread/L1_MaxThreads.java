package com.for_comprehension.reactor.l1_thread;

class L1_MaxThreads {

    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            int finalI = i;
            Thread t = new Thread(() -> {
                try {
                    System.out.println("starting i " + finalI);
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                }
            });

            t.start();
        }
    }
}
