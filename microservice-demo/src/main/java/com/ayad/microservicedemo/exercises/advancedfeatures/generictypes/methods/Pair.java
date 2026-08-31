package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.methods;

import java.util.function.BiFunction;

public record Pair<T, U>(T first, U second) {


    // here we can use the generic we defined in the class level
    // also I defined generic type in the method level, so I can use it as well
    // please note generic that used in the class can only be used in instance methods not static methods
    public <V> Pair<V, U> withFirst(V newFirst) {
        return new Pair<>(newFirst, second);
    }

    // can not use the generic types in the class level as it is only use in instance methods
    // ,but you can use your own generic methods defined
    public static <K, V> Pair<K, V> of(K first, V newSecond) {

        return new Pair<>(first, newSecond);
    }


    public <V, W> Pair<V, W> map(BiFunction<T, U, Pair<V, W>> function) {
        return function.apply(first, second);
    }
}
