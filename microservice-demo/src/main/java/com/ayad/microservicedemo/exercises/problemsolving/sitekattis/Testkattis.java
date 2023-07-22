package com.ayad.microservicedemo.exercises.problemsolving.sitekattis;

import java.util.Scanner;

public class Testkattis {

    public static void main(String[] args) {
        System.out.println("hello world");
        Testkattis testkattis = new Testkattis();
        testkattis.triangleArea();
    }

    public void triangleArea() {
        Scanner input = new Scanner(System.in);
        int height = input.nextInt();
        int base = input.nextInt();
        double area = (0.5) * height * base;
        System.out.println(area);

    }

    public String twosStonesWinner() {
        Scanner input = new Scanner(System.in);
        int numberOfStones = input.nextInt();
        if (numberOfStones % 2 == 0) {
            return "Bob";
        } else {
            return "Alice";
        }


    }





}
