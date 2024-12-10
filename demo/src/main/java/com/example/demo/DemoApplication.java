package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    public class HelloController {

        @GetMapping("/hello")
        public Mono<ResponseEntity<Flux<String>>> hello() {
            return Mono.just(ResponseEntity.ok()
              .contentType(MediaType.TEXT_PLAIN)
              .body(Flux.just("Hello", "World", "!!!")));
        }

        @GetMapping(value = "/hello-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<String> hello_stream() {
            return Flux.interval(Duration.ofSeconds(1))
              .map(i -> i.toString());
        }

        @GetMapping(value = "/hello-stream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Mono<ResponseEntity<Flux<String>>> hello_stream2() {
            return Mono.just(ResponseEntity.ok()
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(Flux.interval(Duration.ofSeconds(1))
                .map(i -> i.toString()).take(20)));
        }

        @GetMapping(value = "/hello-stream3", produces = MediaType.APPLICATION_JSON_VALUE)
        public Mono<ResponseEntity<Flux<String>>> hello_stream3() {
            return Mono.just(ResponseEntity.ok()
              .body(Flux.interval(Duration.ofSeconds(1))
                .map(i -> i.toString()).take(20)));
        }
    }
}
