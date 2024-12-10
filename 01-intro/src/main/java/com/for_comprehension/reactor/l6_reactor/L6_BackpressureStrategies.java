package com.for_comprehension.reactor.l6_reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;

class L6_BackpressureStrategies {

    record Latest() {
        public static void main(String[] args) {
            Flux.interval(Duration.ofMillis(1))
              .onBackpressureLatest()
              .zipWith(Flux.interval(Duration.ofSeconds(1)).take(33))
              .log()
              .blockLast();
        }
    }

    record Buffer() {
        public static void main(String[] args) {
            Flux.interval(Duration.ofMillis(100))
              .onBackpressureBuffer(10, s -> {System.out.println(s);})
//              .log()
              .zipWith(Flux.interval(Duration.ofSeconds(1)).take(33))
              .blockLast();
        }
    }
}
