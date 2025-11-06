package com.ayad.microservicedemo.exercises.functionalInterface;

@FunctionalInterface
public interface Greeting {

    String NAME_CONSTANT = "";

    void greeting(String name);

    static void print() {
        System.out.println("hi from print static method");
    }

    default void sayHi(String name) {
        System.out.println("hi from sayHi default method");
    }


}
