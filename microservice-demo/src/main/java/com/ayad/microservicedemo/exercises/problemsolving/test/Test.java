package com.ayad.microservicedemo.exercises.problemsolving.test;

public class Test {

    public static void main(String[] args) {

        System.out.println(isIndexEven("abcdef", 'e'));

    }


    private static boolean isIndexEven(String s, char item) {
        for (int i = 0; i < s.length(); i = i+2) {
            if (s.charAt(i) == item)
                return true;
        }
        return false;
    }
}
