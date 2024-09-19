package com.example.testservice.controller;

//import org.awaitility.Awaitility;
//import org.awaitility.Durations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExternalServiceIT2{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Reset the mock before each test
        Mockito.reset(restTemplate);
    }

    @Test
    public void testCallExternalApiWithRetry() {
        // Arrange
//        when(restTemplate.getForObject(anyString(), eq(String.class)))
//                .thenThrow(new RestClientException("Service Unavailable")) // Simulate failure
//                .thenThrow(new RestClientException("Service Unavailable")) // Simulate failure
//                .thenReturn("Success"); // Simulate success on third attempt

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new ResourceAccessException("Service Unavailable")) // Simulate failure
                .thenThrow(new ResourceAccessException("Service Unavailable")) // Simulate failure
                .thenReturn("Success"); // Simulate success on third attempt

        // Act
        ResponseEntity<String> response = testRestTemplate.getForEntity("http://localhost:" + port + "/api/call-external", String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody());

//        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
//                verify(restTemplate, times(3)).getForObject(anyString(), eq(String.class))
//        );
    }
}
