package com.for_comprehension.reactor.l6_reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.stream.Stream;

class L6_ReactorSelfInducedOverflow {
    record Example_ZipWith() {
        public static void main(String[] args) {
            Flux.interval(Duration.ofMillis(100))
              .zipWith(Flux.interval(Duration.ofSeconds(100)).take(1))
              .log()
              .blockLast();
        }
    }

    record Example_FlatMap() {
        public static void main(String[] args) {
            Flux.interval(Duration.ofMillis(100))
              .flatMap(i -> Flux.interval(Duration.ofSeconds(100)).take(1))
              .log()
              .blockLast();
        }
    }

    record Example_FlatMap2() {
        public static void main(String[] args) {
            Flux.interval(Duration.ofSeconds(1))
              .flatMap(i -> Flux.interval(Duration.ofMillis(100)).log(), 3, 5)
              .log()
              .blockLast();
        }
    }

    record Example_FlatMap3() {
        public static void main(String[] args) {
            Flux<Integer> f1 = Flux.fromIterable(Stream.generate(() -> 1).limit(1000).toList());
            Flux<Integer> f2 = Flux.fromIterable(Stream.generate(() -> 1).limit(1000).toList());

            Integer result = f1
              .flatMap(i -> f2.log().subscribeOn(Schedulers.boundedElastic()))
              .blockLast();
        }
    }
}
