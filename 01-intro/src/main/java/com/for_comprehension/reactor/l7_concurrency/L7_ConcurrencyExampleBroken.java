package com.for_comprehension.reactor.l7_concurrency;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class L7_ConcurrencyExampleBroken {

    static class AvailabilityContext {
        private String key;
        private boolean available;
        private List<String> values = new ArrayList<>();
    }

    record Example() {

        private static final AvailabilityContext context = new AvailabilityContext();

        public static void main(String[] args) throws InterruptedException {
            Thread.ofPlatform().start(() -> {
                Flux.interval(Duration.ofMillis(1))
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
