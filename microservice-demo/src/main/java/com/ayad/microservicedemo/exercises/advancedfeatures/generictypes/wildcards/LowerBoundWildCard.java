package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.wildcards;

import java.util.List;
import java.util.function.Consumer;




public class LowerBoundWildCard {


    // one rule consumer super: use a lower bound wild card ("super")
    // for a parameter is a consumer of data (as output or consume the data)
    public static <E> void forEach(Iterable<E> iterator, Consumer<? super E> consumer) {

        if (iterator != null) {
            iterator.forEach(consumer);
        }

    }

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        forEach(list, (Integer i) -> System.out.println(i));
        // I can not use Number which is the parent of Integer but I can achive this with lower bound wild card
        forEach(list, (Number n) -> System.out.println(n));
    }


}
