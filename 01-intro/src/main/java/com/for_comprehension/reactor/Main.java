package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

class Main {

    public static void main(String[] args) throws InterruptedException {
        Flux.interval(Duration.ofMillis(1))
          .log()
          .subscribe();

        Thread.sleep(10_000);
    }
}
