package com.for_comprehension.reactor.curiosities;

class JavaMemoryModelInPractice {

    // https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.4
    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {

        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                i++;
                add(i, 2);
            }
        });

        backgroundThread.start();
        Thread.sleep(1000);

        stopRequested = true;

    }

    public static int add(int a, int b) {
        return a + b;
    }
}
