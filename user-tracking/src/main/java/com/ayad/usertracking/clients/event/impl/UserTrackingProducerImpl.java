package com.ayad.usertracking.clients.event.impl;

import com.ayad.usertracking.clients.event.ifc.UserTrackingProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserTrackingProducerImpl implements UserTrackingProducer {

    @Value("${kafka.producer.topics.account-info-message}")
    private String topicName;


    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserTrackingProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void publish(String message) {
        kafkaTemplate.send(topicName, message);


    }

    @Override
    public void publish(String key, String message) {
        kafkaTemplate.send(topicName, key, message);

    }
}
