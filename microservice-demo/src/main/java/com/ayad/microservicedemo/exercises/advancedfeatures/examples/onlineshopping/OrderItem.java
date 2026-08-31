package com.ayad.microservicedemo.exercises.advancedfeatures.examples.onlineshopping;

public sealed interface OrderItem permits ProductItem, DiscountItem {
}
