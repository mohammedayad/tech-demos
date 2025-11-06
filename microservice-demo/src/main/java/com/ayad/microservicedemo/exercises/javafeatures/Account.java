package com.ayad.microservicedemo.exercises.javafeatures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public record Account(int id, int customerId, String type, double balance) implements ParentRecord {

    public static final String text = """
            [
                {
                    "id" : 1223,
                    "customerId" : 1
                }
            ]
            """;
    public static String text2 = """
            [
                {
                    "id" : 1223,
                    "customerId" : 1
                }
            ]
            """;

    public static void print(int x, int... variableParameters) {
        System.out.println("X: " + x + " : " + " variableParameters length:" + variableParameters.length);

        
    }

    public void printVariableParameters(int... variableParameters) {
        Arrays.stream(variableParameters).forEach(System.out::println);

    }
}
