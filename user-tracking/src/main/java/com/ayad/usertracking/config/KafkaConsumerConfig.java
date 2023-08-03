package com.ayad.usertracking.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value("${kafka.client-id}")
    private String clientId;
    @Value("${kafka.consumer.group-id}")
    private String consumerGroupId;
    //private CustomRebalanceListener customRebalanceListener;


    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // factory.getContainerProperties().setConsumerRebalanceListener(customRebalanceListener);
        factory.setErrorHandler(new ErrorHandler() {
            @Override
            public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
                               MessageListenerContainer container) {
                handleConsumerError(thrownException, consumer);
            }

            @Override
            public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
                log.error("Caught Exception From : KafkaConfiguration : kafkaListenerContainerFactory : {}", thrownException.getMessage());
                log.info("data : {}", data);
            }

            @Override
            public void handle(Exception thrownException, ConsumerRecord<?, ?> consumerRecord,
                               Consumer<?, ?> consumer) {
                handleConsumerError(thrownException, consumer);
            }
        });
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    private void handleConsumerError(Exception thrownException, Consumer<?, ?> consumer) {
        final String offsetStr = "offset ";
        String s = thrownException.getMessage().split("Error deserializing key/value for partition ")[1]
                .split("\\. If needed, please seek past the record to continue consumption\\.")[0];
        String topic = String.join("-", Arrays.asList(s.split("-")).subList(0, s.split("-").length - 1));
        int offset = Integer.parseInt(s.split(offsetStr)[1]);
        int partition = Integer.parseInt(s.split("-")[s.split("-").length - 1].split(" at")[0]);
        log.warn("Consumed unsupported avro at offset [{}] from topic [{}]", offset, topic);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        consumer.seek(topicPartition, offset + 1l);
    }

    /**
     * This Bean used to handle kafka errors. Throws
     * {@link MethodArgumentNotValidException} when unexpected schema listened. <br>
     */
    @Bean
    public KafkaListenerErrorHandler validationErrorHandler() {
        return (m, e) -> {
            if (e.getMostSpecificCause() instanceof MethodArgumentNotValidException) {
                throw new MethodArgumentNotValidException(m,
                        ((MethodArgumentNotValidException) e.getMostSpecificCause()).getMethodParameter());
            } else {
                return e.getCause();
            }
        };
    }
}
