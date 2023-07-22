package com.ayad.microservicedemo.exercises.problemsolving.frogger;

import java.util.*;

//problem details: https://codereview.stackexchange.com/questions/205259/naq-2018-problem-d-froggie

public class FroggerGame {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int roadRow = input.nextInt();
        int roadCol = input.nextInt();
        int[][] cars = new int[roadRow][3];//each row will contain (row,col,length of the car)
        for (int i = 0; i < roadRow; i++) {//read car info
            for (int j = 0; j < 3; j++) {
                cars[i][j] = input.nextInt();
            }
            if (i % 2 != 0) {//car is odd number
                cars[i][0] = roadCol - cars[i][0] - 1;
            }
        }
        int[] currentPosition = {roadRow, input.nextInt()};
        String moves = input.next();// sequence of moves

        Map<Character, int[]> directions = new HashMap<>();
        directions.put('U', new int[]{-1, 0});//up
        directions.put('D', new int[]{1, 0});//down
        directions.put('L', new int[]{0, -1});//left
        directions.put('R', new int[]{0, 1});//right

        for (int i = 0; i < moves.length(); i++) {
            char c = moves.charAt(i);
            int[] dir = directions.get(c);
            currentPosition[0] += dir[0];
            currentPosition[1] += dir[1];
            int row = currentPosition[0];
            if (row == -1) {
                System.out.println("safe");
                System.exit(0);
            } else if (row >= roadRow) {
                continue;
            }
            int[] car = cars[row];
            int diff = car[0] + car[2] * (i + 1);
            if (row % 2 != 0) {
                diff = car[0] - car[2] * (i + 1);
            }
            if ((diff - currentPosition[1]) % car[1] == 0) {
                System.out.println("squish");
                System.exit(0);
            }
        }
        System.out.println("squish");
    }

}
