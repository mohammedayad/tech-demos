package com.ayad.microservicedemo.exercises.problemsolving.adyen.paymentvelocity;

import java.time.Duration;

/*
    VelocityProvider interface defines methods for tracking and retrieving payment velocity data.
    It includes methods to get the count of card usages within a specified duration and to register new payments.
 */
public interface VelocityProvider {

    int getCardUsageCount(Payment payment, Duration duration, long timestamp);
    void registerPayment(Payment payment, long timestamp);

    static VelocityProvider getProvider() {
        return new VelocityProviderImpl();
    }

    void printCardUsageMap();
}
