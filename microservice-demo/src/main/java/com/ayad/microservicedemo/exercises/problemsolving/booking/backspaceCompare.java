package com.ayad.microservicedemo.exercises.problemsolving.booking;

public class backspaceCompare {


    public static boolean backspaceCompare(String s, String t) {
        return buildString(s).equals(buildString(t));


    }

    private static String buildString(String s) {
        if (!s.contains("#")) { // base case
            return s;
        }

        StringBuilder sb = new StringBuilder(s);
        int firstOccur = sb.indexOf("#");
        if (firstOccur == 0) {
            sb.deleteCharAt(0);
        } else {
            sb.deleteCharAt(firstOccur); // remove #
            sb.deleteCharAt(firstOccur - 1); // remove char before #
        }
        return buildString(sb.toString());
    }

    public static void main(String[] args) {
        //System.out.println(backspaceCompare("ab#c", "ad#c"));
        // System.out.println(backspaceCompare("ab##", "c#d#"));

        System.out.println(backspaceCompare("ab##", "c#d#"));
    }
}
