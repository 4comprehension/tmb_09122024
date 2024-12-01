package com.for_comprehension.reactor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.for_comprehension.reactor.L5_StreamAPIInterop.L0_convertToStream;
import static com.for_comprehension.reactor.L5_StreamAPIInterop.L1_collectFluxIntoImmutableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class L5_StreamAPIInteropTest {

    @Test
    void l0_convertToStream() {
        var flux = Flux.just(1, 2, 3);

        var result = L0_convertToStream(flux).toList();

        assertThat(result).contains(1, 2, 3);
    }

    @Test
    void l1_collectFluxIntoImmutableList() {
        Mono<List<Integer>> result = L1_collectFluxIntoImmutableList(Flux.just(1, 2, 3));

        StepVerifier.create(result)
          .expectNext(List.of(1, 2, 3))
          .verifyComplete();

        assertThatThrownBy(() -> result.block().add(42))
          .isExactlyInstanceOf(UnsupportedOperationException.class);
    }
}
