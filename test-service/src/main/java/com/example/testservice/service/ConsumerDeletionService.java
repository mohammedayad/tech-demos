package com.example.testservice.service;


import com.example.testservice.model.Consumers;
import com.example.testservice.repository.ConsumerBlockEventsRepository;
import com.example.testservice.repository.ConsumerDevicesRepository;
import com.example.testservice.repository.ConsumerEmailOptinEventsRepository;
import com.example.testservice.repository.ConsumerRepository;
import com.example.testservice.utils.ConsumerPowerBIConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumerDeletionService {
    private final ConsumerRepository consumerRepository;
    private final ConsumerBlockEventsRepository consumerBlockEventsRepository;
    private final ConsumerDevicesRepository consumerDevicesRepository;
    private final ConsumerEmailOptinEventsRepository consumerEmailOptinEventsRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void cleanProspectConsumer(String consumerId) {
        try {
            Optional<Consumers> consumerOptional = consumerRepository.findById(consumerId);
            if (consumerOptional.isPresent()) {
                if (ConsumerPowerBIConstants.PROSPECT_CUSTOMER_TYPE.equals(consumerOptional.get().getConsumerType())) {
                    consumerRepository.deleteById(consumerId);
                    consumerBlockEventsRepository.deleteConsumerBlockEventsById(consumerId);
                    consumerDevicesRepository.deleteAllByConsumerId(Collections.singleton(consumerId));
                    consumerEmailOptinEventsRepository.deleteConsumerEmailOptinEventsByConsumerId(consumerId);
                    log.info("Successfully removed prospect consumer and related data for consumer [{}]",
                            consumerId);
                } else {
                    log.warn("consumer [{}] not a prospect. type [{}]", consumerId,
                            consumerOptional.get().getConsumerType());
                }
            } else {
                log.warn("Prospect consumer [{}] not found to delete.", consumerId);
            }
        } catch (Exception e) {
            log.warn("Unexpected error during cleanup of prospect consumer [{}]. Operation will be retried.", consumerId, e);
//            throw new RetryableException("Unexpected error during cleanup of prospect consumer [" + consumerId + "]", e);

        }
    }
}
