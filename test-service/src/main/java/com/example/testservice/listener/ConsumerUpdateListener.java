package com.example.testservice.listener;

import com.example.testservice.exceptions.Retryable;
import com.example.testservice.service.ConsumerDeletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Listener class for handling consumer delete updates received from a Kafka topic.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerUpdateListener {

//    private final ConsumerDeletionService consumerDeletionService;
//
//
//    /**
//     * Method that listens for consumer delete updates from the Kafka topic and processes them.
//     *
//     * @param consumerId     the consumer update delete record received from the Kafka topic
//     * @param acknowledgment the acknowledgment object to acknowledge message consumption
//     */
//    @KafkaListener(topics = "${kafka.topic.consumer-updates}", containerFactory = "consumerUpdatesContainerFactory")
//    public void onConsumerDeleteUpdate(String consumerId,
//                                       final Acknowledgment acknowledgment) {
//        try {
////            if (consumerUpdateDeleteRecord.getUnregistered()) {
////                consumerDeletionService.cleanProspectConsumer(consumerUpdateDeleteRecord.getConsumerId());
////            } else {
////                log.debug("Received consumer [{}] delete update ignored as it not marked as unregistered.",
////                        consumerUpdateDeleteRecord.getConsumerId());
////            }
//            consumerDeletionService.cleanProspectConsumer(consumerId);
//            acknowledgment.acknowledge();
//        } catch (Exception e) {
//            if (e instanceof Retryable) {
//                throw e;
//            }
//            log.error("Error deleting prospect consumer {}", consumerId, e);
//        }
//    }
}
