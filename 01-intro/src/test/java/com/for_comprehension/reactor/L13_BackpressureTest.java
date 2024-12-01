package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static reactor.core.scheduler.Schedulers.newBoundedElastic;

public class L13_BackpressureTest {

    private static final Logger log = LoggerFactory.getLogger(L13_BackpressureTest.class);

//    @Test
    public void whatIsBackpressure() throws Exception {
        //given
        Hooks.onOperatorDebug();
        final Flux<Long> flux = Flux
          .interval(Duration.ofMillis(10))
          .doOnError(e -> log.error("Error", e))
//          .onBackpressureDrop(x -> log.warn("Dropped {}", x))
          .doOnNext(x -> log.info("Emitting {}", x))
          .doOnRequest(n -> log.info("Requested {}", n))
          .publishOn(newBoundedElastic(10, 10, "Subscriber"))
          .doOnNext(x -> log.info("Handling {}", x));

        //when
        flux.subscribe(slowConsumer(), e -> log.error("Opps", e));

        //then
        TimeUnit.SECONDS.sleep(50);
    }

    private static Consumer<Long> slowConsumer() {
        return x -> WorkshopUtils.sleep(Duration.ofMillis(100));
    }
}
