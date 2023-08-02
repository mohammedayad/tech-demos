package com.ayad.microservicedemo.exercises.problemsolving.sitecodility;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SmallestPositiveInteger {

    public static void main(String[] args) {
         int arr[] = {1, 3, 6, 4, 1, 2};
        // int arr[] = {1, 2, 3};
       // int arr[] = {-1, -3};
       // getTheSmallestPositiveNumberNotExist(arr);
        getTheSmallestNumUsingStreams(arr);

    }


    private static int getTheSmallestPositiveNumberNotExist(int[] A) {
        Arrays.sort(A);
        Arrays.stream(A).forEach(System.out::println);
        int smallestNum = A[A.length - 1] + 1;
        System.out.println("+++++++++++++++++++++++++++++++++ ");
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i + 1] - A[i] == 1 || A[i + 1] - A[i] == 0) {
                System.out.println("A[i+1]-A[i]= " + A[i + 1] + "-" + A[i] + "= " + (A[i + 1] - A[i]));
                continue;
            }

            System.out.println("current num: " + A[i]);
            smallestNum = A[i] + 1;
            break;
        }

        if (smallestNum < 0) smallestNum = 1;
        System.out.println("smallestNum: " + smallestNum);
        return smallestNum;
    }

    private static void getTheSmallestNumUsingStreams(int[] A) {
        IntStream.range(0, A.length - 1).filter(i -> !(A[i + 1] - A[i] == 1 || A[i + 1] - A[i] == 0)).map(i -> {
            if (A[i] < 0) {
                return 1;
            } else {
                return A[i] + 1;
            }
        }).findFirst().ifPresent(System.out::println);
    }
}
