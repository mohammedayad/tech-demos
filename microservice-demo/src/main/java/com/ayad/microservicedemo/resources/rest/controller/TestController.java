package com.ayad.microservicedemo.resources.rest.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("api/test/v1")
public class TestController {

    @GetMapping
    public ResponseEntity<String> testHttpGet() {
        log.info("testHttpGet");
        String hostName=System.getenv().getOrDefault("HOSTNAME","unknown");
        return new ResponseEntity<>("Hello from "+ hostName + new Date(), HttpStatus.OK);


    }
}
