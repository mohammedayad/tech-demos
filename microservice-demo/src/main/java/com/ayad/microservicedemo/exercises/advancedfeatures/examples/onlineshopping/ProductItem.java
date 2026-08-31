package com.ayad.microservicedemo.exercises.advancedfeatures.examples.onlineshopping;

import java.math.BigDecimal;

public record ProductItem(String productCode, String description, int quantity, BigDecimal price) implements OrderItem {
}
