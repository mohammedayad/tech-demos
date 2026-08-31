package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.classes;

import java.util.AbstractList;

public class imutableSingleElementList<E> extends AbstractList<E> {

    private final E elemnt;

    public imutableSingleElementList(E elemnt) {
        this.elemnt = elemnt;
    }


    @Override
    public E get(int index) {
        if (index != 0) throw new IndexOutOfBoundsException(index); // because it is just one element
        return this.elemnt;
    }

    @Override
    public int size() {
        return 1;
    }
}
