package com.example.testservice.controller;

import com.example.testservice.service.ConsumerDeletionService;
import com.example.testservice.service.ConsumerUpdatesProducer;
import com.example.testservice.service.ExternalService;
import com.payconiq.consumer.v1.topic.ConsumerUpdatesV1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final ExternalService externalService;
    private final ConsumerDeletionService consumerDeletionService;

    private final ConsumerUpdatesProducer consumerUpdatesProducer;

    public DemoController(ExternalService externalService, ConsumerDeletionService consumerDeletionService, ConsumerUpdatesProducer consumerUpdatesProducer) {
        this.externalService = externalService;
        this.consumerDeletionService = consumerDeletionService;
        this.consumerUpdatesProducer = consumerUpdatesProducer;
    }

    @GetMapping("/call-external/{consumerId}")
    public ResponseEntity<String> callExternalService(@PathVariable String consumerId) {
//        consumerDeletionService.cleanProspectConsumer(consumerId);
//        externalService.callExternalApi();
        ConsumerUpdatesV1.ConsumerUpdate consumer = ConsumerUpdatesV1.ConsumerUpdate.newBuilder().
                setConsumerId(consumerId).setUnregistered(true).build();
        consumerUpdatesProducer.sendMessage(consumer);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/consumer-deletion/{consumerId}")
    public void callConsumerDeletionService(@PathVariable String consumerId) {
        consumerDeletionService.cleanProspectConsumer(consumerId);
        throw new RuntimeException("failed");
    }

}

