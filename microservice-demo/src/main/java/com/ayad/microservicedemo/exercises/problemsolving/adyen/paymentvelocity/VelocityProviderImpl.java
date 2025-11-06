package com.ayad.microservicedemo.exercises.problemsolving.adyen.paymentvelocity;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class VelocityProviderImpl implements VelocityProvider {

    private final ConcurrentHashMap<String, Deque<Long>> cardUsageMap = new ConcurrentHashMap<>();

    // Get the count of card usages for a given payment card within a specified duration ending at the given timestamp
    @Override
    public int getCardUsageCount(Payment payment, Duration duration, long timestamp) {
        log.debug("Getting card usage count for cardToken: {}, duration: {}, timestamp: {}", payment.getCardToken(), duration, timestamp);
        printTimestamps(timestamp);
        Deque<Long> deque = cardUsageMap.get(payment.getCardToken());
        if (deque == null) return 0;
        long windowStart = timestamp - duration.toMillis();
        log.debug("Window start time: {}", windowStart);
        printTimestamps(windowStart);
        synchronized (deque) {
            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                log.debug("Removing timestamp: {} from deque for cardToken: {}", deque.peekFirst(), payment.getCardToken());
                printTimestamps(deque.peekFirst());
                deque.pollFirst();
            }
            log.debug("Card usage count for cardToken: {} is {}", payment.getCardToken(), deque.size());
            return deque.size();
        }

    }

    // Register a new payment by adding its timestamp to the usage deque for the corresponding card token
    @Override
    public void registerPayment(Payment payment, long timestamp) {
        log.debug("Registering payment for cardToken: {} at timestamp: {}", payment.getCardToken(), timestamp);
        printTimestamps(timestamp);
        Deque<Long> deque = cardUsageMap.computeIfAbsent(payment.getCardToken(), k -> new ArrayDeque<>());
        synchronized (deque) {
            deque.addLast(timestamp);
        }


    }

    // Print the current card usage map for debugging purposes
    @Override
    public void printCardUsageMap() {
        log.debug("Current card usage map:");
        for (var entry : cardUsageMap.entrySet()) {
            log.debug("CardToken: {}, Timestamps: {}", entry.getKey(), entry.getValue());
        }
    }

    // Helper method to print formatted timestamps for debugging
    private void printTimestamps(Long timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String formattedTime = formatter.format(Instant.ofEpochMilli(timestamp));
        log.debug("formattedTime: ({})", formattedTime);
    }
}
