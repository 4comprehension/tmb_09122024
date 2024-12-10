package com.for_comprehension.reactor.l6_reactor;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

// https://www.reactivemanifesto.org
class L6_ReactorSubscribe {

    record Example_CompletableFutureEager() {
        public static void main(String[] args) {
            findUserAsync();
        }
    }

    record Example_MonoLazy() {
        public static void main(String[] args) throws InterruptedException {
            Mono<User> user = findUserReactive()
              .map(i -> {
                  System.out.println("map");
                  return i;
              });
            // nothing happens until we subscribe

            user.subscribe(e -> System.out.println(e));

            Thread.sleep(1000);
        }
    }

    record Example_MonoLazy2() {
        public static void main(String[] args) throws InterruptedException {
            Optional<User> user = findUserReactive()
              .map(i -> {
                  System.out.println("map");
                  return (User) null;
              })
              .blockOptional();

            System.out.println("user = " + user);
        }
    }

    record Example_Threads1() {
        public static void main(String[] args) throws InterruptedException {
            // everything runs on the subscriber thread - main
            findUserReactive()
              .log()
              .map(i -> {
                  System.out.println("2 Thread.currentThread().getName() = " + Thread.currentThread().getName());
                  return i;
              })
              .block();
        }
    }

    record Example_Threads2() {
        public static void main(String[] args) throws InterruptedException {
            // findUserReactive runs on the main thread because it's subscription is on the main thread
            findUserReactive()
              .log()
              // switches from main thread to 'bounded2'
              .publishOn(Schedulers.newBoundedElastic(20, 20, "bounded2"))
              .map(i -> {
                  System.out.println("1 Thread.currentThread().getName() = " + Thread.currentThread().getName());
                  return i;
              })
              .map(i -> {
                  System.out.println("2 Thread.currentThread().getName() = " + Thread.currentThread().getName());
                  return i;
              })
              .block();
        }
    }

    record Example_Threads3() {
        public static void main(String[] args) throws InterruptedException {
            User block = findUserReactive()
//              .log()
              .map(i -> {
                  System.out.println("1 Thread.currentThread().getName() = " + Thread.currentThread().getName());
                  return i;
              })
              .map(i -> {
                  System.out.println("2 Thread.currentThread().getName() = " + Thread.currentThread().getName());
                  return i;
              })
              .log()
              .subscribeOn(Schedulers.newBoundedElastic(20, 20, "subscribe-on-thread-pool"))
              .block();
        }
    }

    record Example_Operator_Fusion() {
        public static void main(String[] args) {
            Mono.fromCallable(() -> {
                  System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                  return new User("John", 42);
              })
              .publishOn(Schedulers.boundedElastic())
              .doOnNext(i -> System.out.println("thread: " + Thread.currentThread().getName()))
              .block();
        }
    }

    record User(String name, int age) {
    }

    static User findUser() {
        System.out.println("finding user");
        return new User("John", 42);
    }

    static CompletableFuture<User> findUserAsync() {
        return CompletableFuture.supplyAsync(L6_ReactorSubscribe::findUser, Executors.newCachedThreadPool());
    }

    static Mono<User> findUserReactive() {
        return Mono.fromCallable(() -> {
            System.out.println("finding user on " + Thread.currentThread().getName());
            return new User("John", 42);
        });
    }
}
