package com.example.testservice.test;

import java.util.HashSet;
import java.util.Set;

// Sliding Window with Two Pointers solution
public class LengthOfLongestSubstringSlideWindowSolution {
    public static void main(String[] args) {
        String s = "abcabcbb";
        int result = lengthOfLongestSubstring(s);
        System.out.println("Length of longest substring: " + result);
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) {
            return 0;  // Edge case: empty string
        }

        // Use a set to store characters in the current window
        Set<Character> seenChars = new HashSet<>();
        int maxLength = 0;
        int left = 0;  // Start of the sliding window

        // Iterate through the string with right pointer
        for (int right = 0; right < s.length(); right++) {
            // If we encounter a repeated character, move the left pointer
            while (seenChars.contains(s.charAt(right))) {
                seenChars.remove(s.charAt(left));
                left++;
            }
            // Add the current character to the set
            seenChars.add(s.charAt(right));

            // Update the maximum length
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}
