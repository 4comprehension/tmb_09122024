package com.for_comprehension.reactor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class L14_RetriesTest {

    @RepeatedTest(50)
    void L1_repeatUntilSuccess() throws Exception {
        var values = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        StepVerifier.create(L14_Retries.L1_repeatUntilSuccess(Flux.fromIterable(values), integer -> Mono.fromCallable(() -> {
            if (ThreadLocalRandom.current().nextInt() % 2 == 0) {
                throw new RuntimeException();
            } else {
                return integer.toString();
            }
        })))
          .expectNext("1", "2", "3", "4", "5", "6", "7", "8")
          .verifyComplete();
    }

    @Test
    void L2_retryIfSlow() {
        AtomicBoolean slow = new AtomicBoolean(true);
        AtomicInteger counter = new AtomicInteger();
        StepVerifier.create(L14_Retries.L2_retryIfSlow(Mono.fromCallable(() -> {
              if (slow.getAndSet(false)) {
                  try {
                      counter.incrementAndGet();
                      Thread.sleep(2000);
                  } catch (InterruptedException e) {
                      // ignore
                  }
                  return 42;
              } else {
                  return 42;
              }
          }).subscribeOn(Schedulers.boundedElastic()), Duration.ofSeconds(1)))
          .thenAwait(Duration.ofMillis(1500))
          .expectNext(42)
          .verifyComplete();

        Assertions.assertThat(counter).hasValue(1);

        StepVerifier.create(L14_Retries.L2_retryIfSlow(Mono.<Integer>error(new RuntimeException()).subscribeOn(Schedulers.boundedElastic()), Duration.ofSeconds(1)))
          .expectError();

    }

}
