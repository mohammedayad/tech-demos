package com.ayad.microservicedemo.exercises.problemsolving.booking;

public class MaxAleternatingSubSequence {

    // nums = [4,2,5,3]
    public long maxAlternatingSum(int[] nums) {
        long evenSum = 0;
        long oddSum = 0;

        for (int num : nums) {
            long newEvenSum = Math.max(evenSum, oddSum + num);
            long newOddSum = Math.max(oddSum, evenSum - num);

            evenSum = newEvenSum;
            oddSum = newOddSum;
        }

        return evenSum;

    }

}
