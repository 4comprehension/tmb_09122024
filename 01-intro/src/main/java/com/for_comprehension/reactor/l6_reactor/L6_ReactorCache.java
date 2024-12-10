package com.for_comprehension.reactor.l6_reactor;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

// https://www.reactivemanifesto.org
class L6_ReactorCache {

    record Example() {
        public static void main(String[] args) {
            // cold source
            Mono<Integer> just = Mono.defer(() -> {
                int data = ThreadLocalRandom.current().nextInt();
                System.out.println("data = " + data);
                return Mono.just(data);
            });

            System.out.println("just.block() = " + just.block());
            System.out.println("just.block() = " + just.block());
            System.out.println("just.block() = " + just.block());
            System.out.println("just.block() = " + just.block());
        }
    }

    record Example2() {
        public static void main(String[] args) {
            UsersHttpClient usersHttpClient = new UsersHttpClient();

            Mono<User> user = Mono.just(usersHttpClient.getUser());
            user.subscribe(System.out::println);
            user.subscribe(System.out::println);
            user.subscribe(System.out::println);
            user.block();
        }
    }

    record Example3() {
        public static void main(String[] args) throws InterruptedException {
            UsersHttpClient usersHttpClient = new UsersHttpClient();

            Mono<User> user = Mono.fromCallable(() -> usersHttpClient.getUser())
                .cache(Duration.ofSeconds(1));
            user.subscribe(System.out::println);
            user.subscribe(System.out::println);
            System.out.println("waiting for cache to expire...");
            Thread.sleep(2000);
            user.subscribe(System.out::println);
            user.block();
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
