package com.reactive.nonblockingapigateway.tenant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class TenantRestAPIs {

    @GetMapping("auchan-api/orders/{orderId}")
    public List<String> auchanOrders(@PathVariable int orderId) {
        return List.of("apple", "orange", "milk");
    }

    @GetMapping("real-api/users")
    public List<String> realUsers() {
        return List.of("adam", "peter", "olga");
    }

    @GetMapping("leclerc-api/opening-hours")
    public String hours() {
        return "7:00->19:00";
    }
}
