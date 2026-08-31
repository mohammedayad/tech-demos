package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.wildcards;

// here we are defining extends while using the wild card element '?'


import java.util.Collection;
import java.util.List;
import java.util.function.Function;

interface Named {
    String name();
}

record Person(String name) implements Named {

}

public class UpperBoundWildCard {


    // if we use this method and called it using List<Person
    // compiler will refuse it because this generic is invariant
    // to fix this you can upper bound wild card
    static void printNames(List<Named> names) {

        for (Named named : names) {
            System.out.println(named.name());
        }

    }

    // one rule producer extend: use a upper bound wild card ("extends")
    // for a parameter is a producer of data
    static void printNamesWithUberBoundWildCard(List<? extends Named> names) {

        for (Named named : names) {
            System.out.println(named.name());
        }

    }

    // type parameter will work correctly but if I used wild card in the return will be a problem
    // as shown in createListWithWildCardReturn when I used it in the main
    public static <N extends Named> List<N> createList(List<String> names, Function<String, N> factory) {

        return names.stream().map(factory).toList();
    }

    // please avoid wild card in the return and it, make ambiguity
    public static List<? extends Named> createListWithWildCardReturn(List<String> names, Function<String, ? extends Named> factory) {

        return names.stream().map(factory).toList();
    }

    public static void main(String[] args) {
        List<Person> persons = List.of(new Person("Mohammed"), new Person("ayad"));
//        printNames(persons); // invariant generic
        printNamesWithUberBoundWildCard(persons);
        List<String> names = List.of("Mohammed", "Ayad");
        // this will work with type parameters
        List<Person> people = createList(names, name -> new Person(name));
        // but when use the method that will return wild card the compiler will not detect this
        // so avoid use wild card in return
//        List<Person> people2 =  createListWithWildCardReturn(names, name -> new Person(name));
    }

}
