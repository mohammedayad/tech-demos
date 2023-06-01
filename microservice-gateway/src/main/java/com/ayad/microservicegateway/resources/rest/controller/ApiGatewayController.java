package com.ayad.microservicegateway.resources.rest.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("api/gateway/v1")
public class ApiGatewayController {

    private final RestTemplate restTemplate;

    public ApiGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<String> test() {
        log.info("test");
        String url="http://microservice-demo:8080/api/test/v1";
        String hostName=System.getenv().getOrDefault("HOSTNAME","unknown");
        ResponseEntity<String> response=restTemplate.getForEntity(url,String.class);
        return new ResponseEntity<>("Hello from "+ hostName + " response "+response.getBody(), HttpStatus.OK);


    }
}
