package com.for_comprehension.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;
import java.util.stream.BaseStream;

class L9_Advanced {

    /**
     * Return {@link Flux} representing Fibonacci sequence starting from 0
     *
     * @implNote Fibonacci is: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144...
     */
    static Flux<Long> L1_fibonacci() {
        return Flux.generate(() -> Map.entry(0L, 1L), (state, sink) -> {
            sink.next(state.getKey());
            return Map.entry(state.getValue(), state.getKey() + state.getValue());
        });
    }

    /**
     * Read "file.txt" into a {@link Flux} and remember to clean up resources
     *
     * @implNote standard Java APIs are blocking, this needs to be addressed
     */
    static Flux<String> L2_readFile() {
        // BlockHound.install();

        return Flux.using(
            () -> Files.lines(new File(L1_HelloWorld.class.getResource("/file.txt").toURI()).toPath()),
            Flux::fromStream,
            BaseStream::close)
          .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Sum all values that were emitted during each second
     */
    static Flux<Long> L3_sumMetricsOverTime(Flux<Long> metrics) {
        return metrics
          .window(Duration.ofSeconds(1))
          .concatMap(window -> window.reduce(0L, Long::sum));
    }
}
