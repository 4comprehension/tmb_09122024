package com.for_comprehension.reactor.curiosities;

import java.util.Arrays;

class StackTraces {

    // XX:-OmitStackTraceInFastThrow
    public static void main(String[] args) {
        String input = null;
        StackTraceElement[] previous = new StackTraceElement[0];

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                input.toLowerCase();
            } catch (Exception e) {
                if (!Arrays.equals(previous, e.getStackTrace())) {
                    if (i != 0) {
                        System.out.println("tracktrace changed");
                    }
                    e.printStackTrace();
                }
                previous = e.getStackTrace();
                if (e.getStackTrace().length == 0) {
                    System.out.println(i);
                    break;
                }
            }
        }
    }
}
