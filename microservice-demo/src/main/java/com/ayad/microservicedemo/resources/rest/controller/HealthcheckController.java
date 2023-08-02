package com.ayad.microservicedemo.resources.rest.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
class HealthcheckController {

    // Your solution

    @GetMapping(value = "/healthcheck")
    public ResponseEntity<Map> healthcheck(@RequestParam(required = false) String format) {
        Map<String, Object> response = new HashMap<>();

        if ("full".equals(format)) {
            // Create a response with current time and status
            String currentTime = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            response.put("currentTime", currentTime);
            response.put("status", "OK");
        } else if ("short".equals(format)) {
            response.put("status", "OK");

        } else {
            response.put("error", "Missing format parameter. Use 'short' or 'full'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/healthcheck")
    public ResponseEntity<String> healthcheckPut() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Method Not Allowed for this endpoint. Use GET method.");
    }

    @PostMapping(value = "/healthcheck")
    public ResponseEntity<String> healthcheckPost() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Method Not Allowed for this endpoint. Use GET method.");
    }


    @DeleteMapping(value = "/healthcheck")
    public ResponseEntity<String> healthcheckDelete() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Method Not Allowed for this endpoint. Use GET method.");
    }

}

