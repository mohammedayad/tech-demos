package com.example.testservice.config.serdes;

import com.payconiq.customer.v1.topic.CustomerUpdatesV1;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ConsumerDataBatchSerializer implements Serializer<CustomerUpdatesV1.Consumer> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // empty
    }

    @Override
    public byte[] serialize(String topic, CustomerUpdatesV1.Consumer consumerUpdate) {
        return consumerUpdate.toByteArray();
    }

    @Override
    public void close() {
        // empty
    }
}
