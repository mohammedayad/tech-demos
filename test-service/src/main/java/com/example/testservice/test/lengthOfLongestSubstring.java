package com.example.testservice.test;

import java.util.*;
import java.util.stream.Collectors;

public class lengthOfLongestSubstring {

    static List<String> finalSubString = new ArrayList<>();

    public static void main(String[] args) {
        lengthOfLongestSubstring("abcabcbb");
    }

    public static int lengthOfLongestSubstring(String s) {
        List<Character> subString = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (!subString.isEmpty() && subString.contains(s.charAt(i))) {
                saveLengthAndSubString(subString);
            }
            subString.add(s.charAt(i));//push
        }
        System.out.println(finalSubString);
        Optional<String> longestSubString = finalSubString.stream()
                .max(Comparator.comparingInt(String::length));
        System.out.println(longestSubString.get());
        System.out.println(longestSubString.get().length());
        int res = longestSubString.map(String::length).orElse(0);
        System.out.println(res);
        return res;

    }

    private static void saveLengthAndSubString(List<Character> subString) {
        finalSubString.add(
                subString.stream().map(String::valueOf).collect(Collectors.joining()));
        subString.clear();
    }


}

