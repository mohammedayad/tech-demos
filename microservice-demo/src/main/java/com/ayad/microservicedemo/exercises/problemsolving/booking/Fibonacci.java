package com.ayad.microservicedemo.exercises.problemsolving.booking;

public class Fibonacci {

    public static int fib(int n) {
        if (n <= 1) return n;

        return fib(n - 1) + fib(n - 2);
    }

    public static int fibDP(int n) {

        int f = 1, s = 1;
        for (int i = 2; i < n; i++) {
            int temp = s;
            s = f + s;
            f = temp;
        }
        return s;
    }


    public static void main(String[] args) {
        System.out.println(fib(3));
//        System.out.println(fibDP(3));
    }
}
