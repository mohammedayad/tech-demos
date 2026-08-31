package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes;

import com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.methods.Pair;

public class Test {

    public static void main(String[] args) {
        var pair = Pair.<Integer, String>of(1, "test");
        var pair2 = Pair.of(1, "test");

        var pair3 = pair.withFirst("test2");


        Pair<Integer, String> test = Pair.of(1, "test");
        test.map((integer, s) -> Pair.of(1, "test"));

        var test1 = Pair.<Integer, String>of(1, "test").map((integer, s) -> Pair.<Integer, Integer>of(1, 2));


    }
}
