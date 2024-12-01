package com.reactive.nonblockingapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NonblockingApiGatewayApplication {

	// curl http://localhost:8080/tenant/leclerc/opening-hours
	// curl http://localhost:8080/tenant/real/users
	// curl http://localhost:8080/tenant/auchan/orders/42
	public static void main(String[] args) {
		SpringApplication.run(NonblockingApiGatewayApplication.class, args);
	}

}
