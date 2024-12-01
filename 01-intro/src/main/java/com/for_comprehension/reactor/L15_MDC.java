package com.for_comprehension.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.function.Consumer;

class L15_MDC {

    private static final Logger log = LoggerFactory.getLogger(L15_MDC.class);

    public static void main(String[] args) {
    }

    private static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (signal.isOnNext()) {
                System.out.println(signal.getContextView());
                signal.getContextView().<String>getOrEmpty("traceId").ifPresentOrElse(uid -> {
                    try (var closeable = MDC.putCloseable("traceId", uid)) {
                        logStatement.accept(signal.get());
                    }
                }, () -> logStatement.accept(signal.get()));
            } else {
                return;
            }
        };
    }
}
