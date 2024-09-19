package com.example.testservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.payconiq.customer.v1.topic.CustomerUpdatesV1.Consumer;


@Slf4j
@Service
public class ConsumerDataBatchProducer {

    private final KafkaTemplate<String, Consumer> kafkaTemplate;
    private final String topicName;

    public ConsumerDataBatchProducer(KafkaTemplate<String, Consumer> kafkaTemplate,
                                     @Value("${kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(Consumer consumer) {
        log.info("start send new consumer {}", consumer.getId());
        kafkaTemplate.send(topicName, consumer);
        log.info("end send new consumer {}", consumer.getId());
    }
}
