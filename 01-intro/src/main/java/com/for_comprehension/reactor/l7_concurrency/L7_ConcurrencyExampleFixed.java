package com.for_comprehension.reactor.l7_concurrency;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class L7_ConcurrencyExampleFixed {

    static class AvailabilityContext {
        private volatile String key;
        private volatile boolean available;
        private volatile List<String> values = Collections.synchronizedList(new ArrayList<>());
    }

    record Example() {

        private static final AvailabilityContext context = new AvailabilityContext();

        public static void main(String[] args) throws InterruptedException {
            Thread.ofPlatform().start(() -> {
                Flux.interval(Duration.ofMillis(1))
                  .publishOn(Schedulers.boundedElastic())
                  .doOnNext(i -> {
                      context.available = i % 2 == 0;
                      context.key = context.key + i;
                      context.values.add("" + i);
                  }).take(100).blockLast();
            });


            Thread.ofPlatform().start(() -> {
                Flux.interval(Duration.ofMillis(1))
                  .doOnNext(i -> {
                      context.available = !context.available;
                      context.key = context.key + i;
                      context.values.add("" + i);
                  }).take(100).blockLast();
            });

            Thread.sleep(1000);

            context.values.stream()
              .collect(Collectors.groupingBy(s -> s, Collectors.counting())).entrySet()
              .stream()

              .filter(e -> e.getValue() < 2)
              .forEach(System.out::println);



        }
    }
}
