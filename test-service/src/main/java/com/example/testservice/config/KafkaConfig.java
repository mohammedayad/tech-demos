package com.example.testservice.config;


import com.example.testservice.config.serdes.ConsumerDataBatchSerializer;
import com.example.testservice.config.serdes.ConsumerUpdatesSerializer;
import com.example.testservice.exceptions.RetryableException;
import com.example.testservice.service.ConsumerUpdatesProducer;
import com.payconiq.consumer.v1.topic.ConsumerUpdatesV1;
import com.payconiq.customer.v1.topic.CustomerUpdatesV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
@Slf4j
public class KafkaConfig {

    @Value("${kafka.consumer.concurrency}")
    private int kafkaConsumerConcurrency;

    @Value("${kafka.consumer.backoff-period}")
    private long backoffPeriod;

    @Value("${kafka.consumer.max-retry-attempts}")
    private long maxRetryAttempts;

    private final KafkaProperties kafkaProperties;

//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Consumer>>
//    consumerContainerFactory(KafkaProperties kafkaProperties) {
//        var factory = new ConcurrentKafkaListenerContainerFactory<String, Consumer>();
//        factory.setConsumerFactory(consumerFactory(kafkaProperties));
//        factory.setBatchListener(true);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        factory.setCommonErrorHandler(
//                new DefaultErrorHandler(new FixedBackOff(backoffPeriod, Long.MAX_VALUE)));
//        factory.setConcurrency(kafkaConsumerConcurrency);
//        return factory;
//    }


    /**
     * Configures and returns a KafkaListenerContainerFactory for handling consumer update messages.
     *
     * @param configurer      the configurer to configure the Kafka listener container factory
     * @param kafkaProperties the Kafka properties for the consumer factory
     * @return a configured ConcurrentKafkaListenerContainerFactory for ConsumerUpdate messages
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> consumerUpdatesContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer, KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerUpdatesConsumerFactory(kafkaProperties));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(kafkaConsumerConcurrency);
        factory.setCommonErrorHandler(errorHandler());
        configurer.configure((ConcurrentKafkaListenerContainerFactory) factory,
                (ConsumerFactory) factory.getConsumerFactory());

        return factory;
    }

    private ConsumerFactory<String, String> consumerUpdatesConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        Deserializer<String> keyDeserializer = Serdes.String().deserializer();
        Deserializer<String> valueDeserializer = Serdes.String().deserializer();
//        Deserializer<ConsumerUpdatesV1.ConsumerUpdate> valueDeserializer = new ConsumerUpdateDeserializer();

        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

//    private ConsumerFactory<String, Consumer> consumerFactory(KafkaProperties kafkaProperties) {
//        var props = kafkaProperties.buildConsumerProperties();
//
//        var keyDeserializer = Serdes.String().deserializer();
//        var valueDeserializer = new ConsumerDataKafkaDeserializer();
//
//        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
//    }


    /**
     * Configures and returns a DefaultErrorHandler for handling errors during Kafka message processing.
     *
     * @return a configured DefaultErrorHandler with a fixed back-off policy and specific retryable exceptions
     */
    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff fixedBackOff = new FixedBackOff(backoffPeriod, maxRetryAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) ->
                log.warn("Retry attempts are exhausted for event: {}", consumerRecord.toString()), fixedBackOff
        );
        errorHandler.addRetryableExceptions(RetryableException.class);
        return errorHandler;
    }


    @Bean
    public ProducerFactory<String, CustomerUpdatesV1.Consumer> consumerDataBatchProducerFactory() {
        Map<String, Object> producerProperties = this.kafkaProperties.buildProducerProperties();
        producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, "consumer-batch");
        return new DefaultKafkaProducerFactory<>(producerProperties, Serdes.String().serializer(),
                new ConsumerDataBatchSerializer());
    }

    @Bean
    public KafkaTemplate<String, CustomerUpdatesV1.Consumer> consumerDataBatchKafkaTemplate() {
        return new KafkaTemplate<>(consumerDataBatchProducerFactory());
    }


    @Bean
    public ProducerFactory<String, ConsumerUpdatesV1.ConsumerUpdate> consumerUpdatesProducerFactory() {
        Map<String, Object> producerProperties = this.kafkaProperties.buildProducerProperties();
        producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, "consumer-updates");
        return new DefaultKafkaProducerFactory<>(producerProperties, Serdes.String().serializer(),
                new ConsumerUpdatesSerializer());
    }

    @Bean
    public KafkaTemplate<String, ConsumerUpdatesV1.ConsumerUpdate> consumerUpdatesKafkaTemplate() {
        return new KafkaTemplate<>(consumerUpdatesProducerFactory());
    }


//    @Bean
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return props;
//    }
//
//
//    @Bean
//    public ProducerFactory<String, CustomerUpdatesV1.Consumer> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    public KafkaTemplate<String, CustomerUpdatesV1.Consumer> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }

}
