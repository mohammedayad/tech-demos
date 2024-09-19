package com.example.testservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalService {

    private final RestTemplate restTemplate;

    private int retryCount;

    public ExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void callExternalApi() {
        ++retryCount;
        String url = "https://api.example.com/resource";  // Replace with actual URL
        log.info("retry num: {}", retryCount);
        restTemplate.getForObject(url, String.class);
    }
}

