package com.ayad.microservicedemo.exercises.problemsolving.booking;

public class MaxmalSquare {

    public static int maxSquare(char[][] matrix) {

        int rowsLen = matrix.length;
        int colsLen = matrix[0].length;
        int dp[][] = new int[rowsLen][colsLen];





        int maxSideLength = 0;
        for (int i = 0; i < rowsLen; i++) { // rows
            for (int j = 0; j < colsLen; j++) { //cols
                if (matrix[i][j] == '1') {
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                    }
                    maxSideLength = Math.max(maxSideLength, dp[i][j]);
                }


            }

        }
        return maxSideLength * maxSideLength;
    }


    public static void main(String[] args) {
//        int[][] matrix = {
//                {0, 1, 1, 0, 1},
//                {1, 1, 0, 1, 0},
//                {0, 1, 1, 1, 0},
//                {1, 1, 1, 1, 0},
//                {1, 1, 1, 1, 1},
//                {0, 0, 0, 0, 0}
//        };
//        int[][] matrix = {
//                {0, 1},
//                {1, 0},
//                {1, 1},
//                {1, 1}
//        };

        char[][] matrix = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };

        System.out.println(maxSquare(matrix));
    }
}
