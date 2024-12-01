package com.reactive.nonblockingapigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProxyHandler {

    private final WebClient webClient;
    private final RedisPathResolver tenantPathResolver = new RedisPathResolver();

    public ProxyHandler(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public Mono<ServerResponse> proxyRequest(ServerRequest request) {
        var tenant = request.pathVariable("id");
        var path = request.path().substring(("/tenant/" + tenant).length());
        return tenantPathResolver.resolveFor(tenant)
          .flatMap(basePath -> webClient
            .get()
            .uri(basePath + path)
            .retrieve()
            .bodyToMono(String.class))
          .log()
          .flatMap(responseBody -> ServerResponse.ok().bodyValue(responseBody))
          .onErrorResume(e -> ServerResponse.badRequest().body(Mono.just(e.getMessage()), String.class));
    }

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
          .GET("/tenant/{id}/**", this::proxyRequest)
          .build();
    }
}
