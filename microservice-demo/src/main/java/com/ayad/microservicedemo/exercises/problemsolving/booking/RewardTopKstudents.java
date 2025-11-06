package com.ayad.microservicedemo.exercises.problemsolving.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RewardTopKstudents {

    public static List<Integer> topStudents(String[] positive_feedback,
                                            String[] negative_feedback,
                                            String[] report,
                                            int[] student_id, int k) {

        Map<Integer, Integer> studentsPoints = new TreeMap<>();

        // check each student
        for (int i = 0; i < student_id.length; i++) { // o(n)
            int studentId = student_id[i];
            String studentReport = report[i];
            int studentPoints = calcStudentPoints(studentReport, positive_feedback, negative_feedback);
            studentsPoints.put(studentId, studentPoints);
        }

        return studentsPoints.entrySet().stream().sorted(
                (e1, e2) -> {
                    int compareValue = e2.getValue().compareTo(e1.getValue());// pints desc
                    if (compareValue == 0) {
                        return e1.getKey().compareTo(e2.getKey()); // id asc
                    }
                    return compareValue;
                }


        ).map(Map.Entry::getKey).limit(k).toList();


    }

    private static int calcStudentPoints(String studentReport, String[] positive_feedback, String[] negative_feedback) {
        int positiveLength = positive_feedback.length;
        int negativeLength = negative_feedback.length;
        int studentPoints = 0;
        int loopLen = Math.max(positiveLength, negativeLength);
        for (int i = 0; i < loopLen; i++) {
            if (studentReport.contains(positive_feedback[i])) {
                studentPoints += 3;
            }
            if (i < negativeLength) {
                if (studentReport.contains(negative_feedback[i])) {
                    studentPoints -= 1;
                }

            }
        }
        return studentPoints;
    }

    public static void main(String[] args) {
        String[] positive_feedback = {"smart", "brilliant", "studious"};
        String[] negative_feedback = {"not"};
        String[] report = {"this student is not studious", "the student is smart"};
        int[] studentId = {1, 2};
        int k = 2;
        System.out.println(topStudents(positive_feedback, negative_feedback, report, studentId, k));
    }
}
