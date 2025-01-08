package com.ayad.integrationservice.resources.rest.controller;


import com.ayad.integrationservice.domain.model.dtos.ProcessingResult;
import com.ayad.integrationservice.domain.service.ifc.CVProcessorService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("integration-service/v1/api")
public class CVProcessorController {

    private final Tracer tracer;


    private final CVProcessorService processorService;

    public CVProcessorController(Tracer tracer, CVProcessorService processorService) {
        this.tracer = tracer;
        this.processorService = processorService;
    }


    @PostMapping("/submit")
    public ResponseEntity<String> submitCV(@RequestParam("file") MultipartFile file) {
        log.info("Received request to submit CV: {}", file.getOriginalFilename());
        String processId = processorService.submitCV(file);
        log.info("CV {} submitted successfully. Process ID: {}", file.getOriginalFilename(), processId);
        return ResponseEntity.ok(processId);
    }

    @GetMapping("/retrieve/{processId}")
    public ResponseEntity<ProcessingResult> retrieveResult(@PathVariable String processId) {
        log.info("Fetching processing result for Process ID: {}", processId);
        ProcessingResult result = processorService.getProcessingResult(processId);
        log.info("Process ID {} Fetched successfully. with status {}", processId, result.getStatus());
        return ResponseEntity.ok(result);
    }


    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader Map<String, String> headers) {
        log.info("request received");
        // Log all headers
        headers.forEach((key, value) -> {
            log.info("Header Key: {} Value: {}" ,key, value);
        });
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            String traceId = currentSpan.context().traceId();
            String spanId = currentSpan.context().spanId();
            log.info("Trace ID: {}" , traceId);
            log.info("Span ID: {}" , spanId);
        } else {
            log.info("No active span found.");
        }
        return ResponseEntity.ok("success");
    }

}
