package com.ayad.microservicedemo.exercises.advancedfeatures.enums;

import java.util.function.DoubleBinaryOperator;

public enum MathOperationsWithLambda {
    SUBTRACT((x, y) -> x - y),
    ADD(Double::sum),
    DIVIDE((x, y) -> x / y),
    MULTIPLY((x, y) -> x * y);

    private final DoubleBinaryOperator operator;

    MathOperationsWithLambda(DoubleBinaryOperator operator) {
        this.operator = operator;
    }

    public double calculate(double x, double y) {
        return this.operator.applyAsDouble(x, y);
    }
}
