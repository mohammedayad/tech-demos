package com.ayad.usertracking.resources.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserTrackingConsumer {


    @KafkaListener(topics = {
            "${kafka.consumer.topics.account-info-message}"}, errorHandler = "validationErrorHandler")
    public void consume(ConsumerRecord<String, String> message) {
        log.info("consumer consumed new message with key {} and value {}", message.key(), message.value());


    }
}
