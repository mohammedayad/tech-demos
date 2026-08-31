package com.ayad.microservicedemo.exercises.advancedfeatures.generictypes.wildcards;

import java.util.ArrayList;
import java.util.List;

public class UnboundWildCard {

    // here we are not depend on E, so we can use the unbounded wild card symbol "?" here for simplicity
    public <E> int size(List<E> list) {
        return list != null ? list.size() : 0;
    }

    public int sizeWithUnboundCard(List<?> list) {
        return list != null ? list.size() : 0;
    }

    // here we can not add element as the compiler, don not know what is the type
    // it can be, list of Strings, Integer, etc
    public void test() {
        List<?> list = new ArrayList<String>();
//        list.add("test");
        list.add(null); // but you are allowed to add null
        List<String> list2 = new ArrayList<>();
        list2.add("test");


    }
}
