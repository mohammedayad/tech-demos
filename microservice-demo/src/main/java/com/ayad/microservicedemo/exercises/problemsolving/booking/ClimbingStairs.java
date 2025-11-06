package com.ayad.microservicedemo.exercises.problemsolving.booking;

public class ClimbingStairs {

    public static int cb(int n){

        if(n<=1) return 1;

        return cb(n-1)+cb(n-2);
    }


    public static void main(String[] args) {
        System.out.println(cb(3));
    }
}
