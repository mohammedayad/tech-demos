package com.ayad.integrationservice.clients.proxy.impl;

import com.ayad.integrationservice.clients.proxy.ifc.TkExtractionClient;
import com.ayad.integrationservice.config.TkExtractionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TkExtractionClientImpl implements TkExtractionClient {
    private final RestTemplate restTemplate;
    private final TkExtractionProperties tkExtractionProperties;

    public TkExtractionClientImpl(RestTemplate restTemplate, TkExtractionProperties tkExtractionProperties) {
        this.restTemplate = restTemplate;
        this.tkExtractionProperties = tkExtractionProperties;
    }

    @Async
    @Override
    public CompletableFuture<String> callTKExtractionService(String filePath) {
        try {
            // Create a FileSystemResource from the saved file path
            FileSystemResource fileResource = new FileSystemResource(new File(filePath));
            log.info("Calling TK Extraction Service with file: {}", fileResource.getFilename());
            // Create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Prepare the form data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("account", tkExtractionProperties.getAccount());
            body.add("username", tkExtractionProperties.getUsername());
            body.add("password", tkExtractionProperties.getPassword());
            body.add("uploaded_file", fileResource);

            // Create an HTTP entity containing the headers and form data
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Make the POST request to the TK Extraction service
            ResponseEntity<String> response = restTemplate.exchange(
                    tkExtractionProperties.getUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // If the response is successful, return the result (XML)
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("TK Extraction Service successfully processed the file: {}", fileResource.getFilename());  // Success log
                return CompletableFuture.completedFuture(response.getBody());  // Return the result asynchronously
            } else {
                log.error("TK Extraction Service failed with status: {}", response.getStatusCode());  // Error log
                return CompletableFuture.failedFuture(new RuntimeException("Failed to call TK Extraction Service: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            log.error("Error occurred while calling TK Extraction Service for file path: {}", filePath, e);  // Error log
            return CompletableFuture.failedFuture(new RuntimeException("Error occurred while calling TK Extraction Service", e));
        }

    }
}
