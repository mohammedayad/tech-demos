package com.example.testservice.controller;


//import org.awaitility.Awaitility;
//import org.awaitility.Durations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExternalServiceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Reset the mock before each test
        Mockito.reset(restTemplate);
    }

    @Test
    public void testCallExternalApiWithRetry() throws Exception {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RestClientException("Service Unavailable")) // Simulate failure
                .thenThrow(new RestClientException("Service Unavailable")) // Simulate failure
                .thenReturn("Success"); // Simulate success on third attempt

        // Act
        mockMvc.perform(get("/api/call-external")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // Assert
//        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
//                verify(restTemplate, times(3)).getForObject(anyString(), eq(String.class))
//        );
    }
}
