package com.ayad.microservicedemo.exercises.advancedfeatures.enums;

public enum MathOperationsWithOopPolymorphism {

    SUBTRACT {
        @Override
        public double calculate(double x, double y) {
            return x - y;
        }
    },
    ADD {
        @Override
        public double calculate(double x, double y) {
            return x + y;
        }
    },
    DIVIDE {
        @Override
        public double calculate(double x, double y) {
            return x / y;
        }
    },

    MULTIPLY {
        @Override
        public double calculate(double x, double y) {
            return x * y;
        }


    };

    public abstract double calculate(double x, double y);

}