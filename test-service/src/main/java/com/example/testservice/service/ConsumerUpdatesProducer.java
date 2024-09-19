package com.example.testservice.service;

import com.payconiq.consumer.v1.topic.ConsumerUpdatesV1;
import com.payconiq.customer.v1.topic.CustomerUpdatesV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerUpdatesProducer {

    private final KafkaTemplate<String, ConsumerUpdatesV1.ConsumerUpdate> kafkaTemplate;
    private final String topicName;

    public ConsumerUpdatesProducer(KafkaTemplate<String, ConsumerUpdatesV1.ConsumerUpdate> kafkaTemplate,
                                     @Value("${kafka.topic.consumer-updates}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(ConsumerUpdatesV1.ConsumerUpdate consumer) {
        log.info("start send delete consumer update {}", consumer.getConsumerId());
        kafkaTemplate.send(topicName, consumer);
        log.info("end send delete consumer update {}", consumer.getConsumerId());
    }
}
