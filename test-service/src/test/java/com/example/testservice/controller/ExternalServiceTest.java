package com.example.testservice.controller;


import com.example.testservice.service.ExternalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableRetry
public class ExternalServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalService externalService;

//    public ExternalServiceTest() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void testCallExternalApiWithRetry() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RestClientException("Service Unavailable")) // Simulate failure
                .thenThrow(new RestClientException("Service Unavailable")) // Simulate failure
                .thenReturn("Success"); // Simulate success on third attempt

        try {
            // Act
            externalService.callExternalApi();
        } catch(Exception e){
            // Assert
            verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));

        }
    }
}

