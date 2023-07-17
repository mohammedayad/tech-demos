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
        String hostName = System.getenv().getOrDefault("HOSTNAME", "unknown");
        return new ResponseEntity<>("Hello from " + hostName + new Date(), HttpStatus.OK);


    }

    public static void main(String[] args) {
        System.out.println(mask("123456789"));
    }

    private static String mask(String creditCard) {
        System.out.println("creditCard len: " + creditCard.length());
        boolean isNumeric = creditCard.matches("-?\\d+(\\.\\d+)?");

        if (!isNumeric) {
            throw new IllegalArgumentException("");
        }
        if (creditCard.length() < 6) return creditCard;
        String maskedCreditCard = "";
        char characterArr[] = creditCard.toCharArray();
        for (int index = 0; index < creditCard.length(); index++) {
            if (index == 0 || (index >= creditCard.length() - 4 && index < creditCard.length())) {
                maskedCreditCard = maskedCreditCard + creditCard.charAt(index);
                continue;
            }
            maskedCreditCard = maskedCreditCard + '*';
        }
        return maskedCreditCard;


    }
}

