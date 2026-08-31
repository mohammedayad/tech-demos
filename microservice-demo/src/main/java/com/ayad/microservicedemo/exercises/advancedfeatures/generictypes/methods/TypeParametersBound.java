package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.methods;

import java.util.Collection;


interface Named {
    String name();
}


// record generate methods call name() and age() as getter so no need to implement it
record Person(String name, int age) implements Named, Comparable<Person> {


    @Override
    public int compareTo(Person o) {
        return Integer.compare(this.age, o.age);
    }
}

public class TypeParametersBound {

    // limit the generic type to types that extends or implements Comparable
    public <T extends Comparable<T>> T Max(Collection<T> collection) {
        T result = null;

        for (T element : collection) {
            if (result == null || element.compareTo(result) > 0) {
                result = element;
            }
        }
        return result;

    }


    public <T extends Named & Comparable<T>> String nameOfMax(Collection<T> collection) {
        T result = null;

        for (T element : collection) {
            if (result == null || element.compareTo(result) > 0) {
                result = element;
            }
        }
        return result != null ? result.name() : null;

    }
}
