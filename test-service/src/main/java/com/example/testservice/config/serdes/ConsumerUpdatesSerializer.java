package com.example.testservice.config.serdes;

import com.payconiq.consumer.v1.topic.ConsumerUpdatesV1;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ConsumerUpdatesSerializer implements Serializer<ConsumerUpdatesV1.ConsumerUpdate> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // empty
    }

    @Override
    public byte[] serialize(String topic, ConsumerUpdatesV1.ConsumerUpdate consumerUpdate) {
        return consumerUpdate.toByteArray();
    }

    @Override
    public void close() {
        // empty
    }
}
