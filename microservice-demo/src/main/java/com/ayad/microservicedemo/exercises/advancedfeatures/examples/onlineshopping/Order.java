package com.ayad.microservicedemo.exercises.advancedfeatures.examples.onlineshopping;

import java.util.List;
import java.util.UUID;

public record Order(UUID orderId, UUID customerId, List<OrderItem> items) {

    public Order {
        items = List.copyOf(items); // Ensure immutability of the items list
    }
}
