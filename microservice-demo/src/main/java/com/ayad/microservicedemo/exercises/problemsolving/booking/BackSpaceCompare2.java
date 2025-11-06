package com.ayad.microservicedemo.exercises.problemsolving.booking;

public class BackSpaceCompare2 {


    public static boolean backSpace(String s1, String s2) {

        int s1Index = s1.length() - 1, s2Index = s2.length() - 1;
        while (s1Index >= 0 && s2Index >= 0) { // o(max(s1,s2)
            s1Index = findProperIndex(s1, s1Index);
            s2Index = findProperIndex(s2, s2Index);
            if (s1Index < 0 && s2Index < 0) return true; // traversed both strings
            if (s1Index < 0 || s2Index < 0 || s1.charAt(s1Index) != s2.charAt(s2Index))
                return false;
            s1Index--;
            s2Index--;


        }
        return true;


    }

    private static int findProperIndex(String s, int index) {

        int backSpaceCount = 0;
        while (index >= 0) {
            if (s.charAt(index) == '#') {
                backSpaceCount++;
            } else if (backSpaceCount > 0) {
                backSpaceCount--;

            } else {
                break;
            }
            index--;
        }
        System.out.println("String: " + s + " proper index: " + index);
        return index;
    }

    public static void main(String[] args) {
        System.out.println(backSpace("ab#c", "ad#c"));

    }
}
