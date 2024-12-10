package com.for_comprehension.reactor.l6_reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

class L6_ReactorHotCold {

    record Example1() {
        public static void main(String[] args) {
            // cold source
            Mono<Integer> just = Mono.just(42);

            just.log().subscribe(System.out::println);


        }
    }

    record Example2() {
        public static void main(String[] args) throws InterruptedException {
            Flux.fromStream(Stream.iterate(0, i -> i + 1))
              .log()
              .subscribe(null, null, null, s -> s.request(5));

            Thread.sleep(100000);
        }
    }

    record Example3() {

        // TODO backpressure
        public static void main(String[] args) {

            Flux<Long> f1 = Flux.interval(Duration.ofMillis(1000));
            Flux<Long> f2 = Flux.interval(Duration.ofMillis(10));

            f1.take(1)
              .flatMap(i -> Mono.fromCallable(() -> "")
                .subscribeOn(Schedulers.boundedElastic()))
              .log()
              .blockLast();

        }
    }

    record Example4() {
        public static void main(String[] args) {
            Flux.just(1,2,3)
              .blockLast();
        }
    }

    static Mono<User> findUserReactive() {
        return Mono.fromCallable(() -> {
            System.out.println("finding user on " + Thread.currentThread().getName());
            return new User("John", 42);
        });
    }

    record User(String name, int age) {
    }

    static class UsersHttpClient {

        // simulating HTTP GET request
        public User getUser() {
            int age = ThreadLocalRandom.current().nextInt(18, 100);
            String name = "User" + ThreadLocalRandom.current().nextInt(1, 1000);
            return new User(name, age);
        }
    }

    interface ReactiveUsersClient {
        Mono<User> getUser();
    }

    static class ReactiveUsersHttpClient implements ReactiveUsersClient {

        // simulating HTTP GET request
        public Mono<User> getUser() {
           return Mono.fromCallable(() -> {
               int age = ThreadLocalRandom.current().nextInt(18, 100);
               String name = "User" + ThreadLocalRandom.current().nextInt(1, 1000);
               return new User(name, age);
           });
        }
    }

    static class CachingReactiveUsersClient implements ReactiveUsersClient {
        private final Object lock = new Object();

        private final ReactiveUsersClient delegate;
        private final Duration cacheDuration;
        private volatile Mono<User> cachedUser;

        public CachingReactiveUsersClient(ReactiveUsersClient delegate, Duration cacheDuration) {
            this.delegate = delegate;
            this.cacheDuration = cacheDuration;
        }

        @Override
        public Mono<User> getUser() {
            if (cachedUser == null) {
                synchronized (lock) {
                    if (cachedUser == null) {
                        cachedUser = delegate.getUser().cache(cacheDuration);
                    }
                }
            }
            return cachedUser;
        }
    }
}
