package com.ayad.microservicedemo.exercises.problemsolving.sitefutureskill;

import java.util.ArrayList;
import java.util.List;

public class SquirrelJump {

    private static int[] indexBarArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static int[] indexBarLengthArray = {6, 4, 14, 6, 8, 13, 9, 7, 10, 6, 12};


//    public int positionAtStep(int step) {
//        // Your implementation logic here
//        // Replace the return statement with your actual calculation.
//        return 0;
//    }


    public static void getMaxNumOfJumps(int stepIndex) {

        int startBarLength = indexBarLengthArray[stepIndex];
        List<Integer> rightResult = new ArrayList<>(indexBarArray.length);
        List<Integer> leftResult = new ArrayList<>(indexBarArray.length);
        rightResult.add(stepIndex);//init index
        //  for (int i = stepIndex + 1; i < indexBarArray.length - 2; i++) {
        int i = stepIndex + 1;

        while (i < indexBarArray.length - 2) {
            int nextRightBarLength = indexBarLengthArray[i];
            int nextNextRightBarLength = indexBarLengthArray[i + 1];

            if (startBarLength > nextRightBarLength) {//handle right
                if (startBarLength > nextNextRightBarLength) {
                    if (nextRightBarLength > nextNextRightBarLength) {//best choice nextRightBarLength
                        rightResult.add(i);
                        startBarLength = indexBarLengthArray[i];

                    } else {//best choice nextNextRightBarLength
                        rightResult.add(i + 1);
                        startBarLength = indexBarLengthArray[i + 1];
                        i++;
                    }
                } else {//best choice nextRightBarLength
                    rightResult.add(i);
                    startBarLength = indexBarLengthArray[i];
                }
            }
//            else if (nextRightBarLength > nextNextRightBarLength) {//best choice nextNextRightBarLength
//                rightResult.add(i + 1);
//                startBarLength = indexBarLengthArray[i + 1];
//                i++;
//            }
            else {
                getMaxNumOfJumps(i);
                break;
            }
        }

        System.out.println("Right direction "+ i + " "+ rightResult);
        int startLeftBarLength = indexBarLengthArray[stepIndex];
        leftResult.add(stepIndex);//left init index
        int j = stepIndex - 1;
        while (j > 0) {//handle left
            int previousLeftBarLength = indexBarLengthArray[j];
            int previousPreviousLeftBarLength = indexBarLengthArray[j - 1];
            if (startLeftBarLength > previousLeftBarLength) {//handle right
                if (startLeftBarLength > previousPreviousLeftBarLength) {
                    if (previousLeftBarLength > previousPreviousLeftBarLength) {//best choice previousLeftBarLength
                        leftResult.add(j);
                        startLeftBarLength = indexBarLengthArray[j];

                    } else {//best choice previousPreviousLeftBarLength
                        leftResult.add(j - 1);
                        startLeftBarLength = indexBarLengthArray[j - 1];
                        j--;
                    }

                } else {//best choice previousLeftBarLength
                    leftResult.add(j);
                    startLeftBarLength = indexBarLengthArray[j];
                }

            }
//            else if (previousLeftBarLength > previousPreviousLeftBarLength) {//best choice nextNextRightBarLength
//                leftResult.add(j - 1);
//                startLeftBarLength = indexBarLengthArray[j - 1];
//                j--;
//            }
            else {
                break;
            }


        }

        System.out.println("left direction "+ j + " "+ leftResult);

    }


        public static void main (String[]args){
            getMaxNumOfJumps(2);

        }
    }
