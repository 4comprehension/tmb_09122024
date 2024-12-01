package com.for_comprehension.reactor;

import java.time.Duration;

final class WorkshopUtils {

    private WorkshopUtils() {
    }

    static <T> T todo() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
