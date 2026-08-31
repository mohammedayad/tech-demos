package com.ayad.microservicedemo.exercises.advancedfeatures.enums;

import java.math.BigDecimal;

public enum MathOperationsWithSingleInstanceMethod {
    SUBTRACT,
    ADD,
    DIVIDE,
    MULTIPLY;


    public double calculate(double x, double y) {
        return switch (this) {
            case ADD -> x + y;
            case DIVIDE -> x / y;
            case MULTIPLY -> x * y;
            case SUBTRACT -> x - y;
        };
    }
}
