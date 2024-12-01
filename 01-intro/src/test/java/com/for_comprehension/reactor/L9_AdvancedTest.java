package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.for_comprehension.reactor.L9_Advanced.L1_fibonacci;
import static com.for_comprehension.reactor.L9_Advanced.L2_readFile;
import static com.for_comprehension.reactor.L9_Advanced.L3_sumMetricsOverTime;

public class L9_AdvancedTest {

    @Test
    public void testL1_fibonacci() {
        StepVerifier.create(L1_fibonacci().take(10))
          .expectNext(0L)
          .expectNext(1L)
          .expectNext(1L)
          .expectNext(2L)
          .expectNext(3L)
          .expectNext(5L)
          .expectNext(8L)
          .expectNext(13L)
          .expectNext(21L)
          .expectNext(34L)
          .verifyComplete();
    }

    @Test
    public void testL2_readFile() {
        StepVerifier.create(L2_readFile())
          .expectNext("foo")
          .expectNext("bar")
          .expectNext("42")
          .verifyComplete();
    }

    @Test
    public void testL3_sumMetricsOverTime() {
        StepVerifier.withVirtualTime(() -> {
              return L3_sumMetricsOverTime(Flux.interval(Duration.ofMillis(400)).take(10));
          }).thenAwait(Duration.ofSeconds(1))
          .expectNext(1L)
          .thenAwait(Duration.ofSeconds(1))
          .expectNext(5L)
          .thenAwait(Duration.ofSeconds(1))
          .expectNext(15L)
          .thenAwait(Duration.ofSeconds(1))
          .expectNext(15L)
          .thenAwait(Duration.ofSeconds(1))
          .expectNext(9L)
          .thenAwait(Duration.ofSeconds(1))
          .verifyComplete();
    }
}
