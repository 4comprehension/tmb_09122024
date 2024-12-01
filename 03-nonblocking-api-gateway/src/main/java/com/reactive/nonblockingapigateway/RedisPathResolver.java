package com.reactive.nonblockingapigateway;

import reactor.core.publisher.Mono;

class RedisPathResolver {

    Mono<String> resolveFor(String tenantId) {
        return switch (tenantId) {
            case "auchan" -> Mono.just("http://localhost:8080/auchan-api");
            case "real" -> Mono.just("http://localhost:8080/real-api");
            case "leclerc" -> Mono.just("http://localhost:8080/leclerc-api");
            default -> Mono.empty();
        };
    }
}
