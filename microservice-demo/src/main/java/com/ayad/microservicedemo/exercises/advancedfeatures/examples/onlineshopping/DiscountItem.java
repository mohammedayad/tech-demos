package com.ayad.microservicedemo.exercises.advancedfeatures.examples.onlineshopping;

import java.math.BigDecimal;

public record DiscountItem(String discountCode, String description, BigDecimal discountAmount) implements OrderItem {
}
