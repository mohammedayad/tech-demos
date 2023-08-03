package com.ayad.usertracking.resources.rest.controller;


import com.ayad.usertracking.domain.ifc.UserTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/usertracking")
public class UserTrackingController {
    private final UserTrackingService userTrackingService;

    public UserTrackingController(UserTrackingService userTrackingService) {
        this.userTrackingService = userTrackingService;
    }

    @GetMapping(value = "/v1/produce/events")
    public ResponseEntity<String> produceEvents() {
        log.info("produceEvents(): Called");
        userTrackingService.produceEvents();
        return new ResponseEntity<>("Events Produced", HttpStatus.OK);
    }
}
