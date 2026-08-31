package com.ayad.microservicedemo.exercises.advancedfeatures.enums;

interface Evaluator {
    double calculate(double x, double y);
}

// enum can not extend it can just implements interface
class ParentClass {

    public void print() {
        System.out.println("called print");
    }

}

enum MathOperationsWithOopPolymorphismWithInterface implements Evaluator {

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
}
