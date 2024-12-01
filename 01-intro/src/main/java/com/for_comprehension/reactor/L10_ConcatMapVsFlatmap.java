package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

class L10_ConcatMapVsFlatmap {

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4)
//          .flatMap(a -> process(a))
//          .flatMapSequential(a -> process(a))
//          .concatMap(a -> process(a))
          .doOnNext(n -> System.out.println("done " + n))
          .then()
          .block();
    }

    private static Mono<Integer> process(Integer number) {
        return number == 2
          ? Mono.just(number).doOnNext(n -> System.out.println("executing " + n)).delayElement(Duration.ofSeconds(1))
          : Mono.just(number).doOnNext(n -> System.out.println("executing " + n));
    }
}
