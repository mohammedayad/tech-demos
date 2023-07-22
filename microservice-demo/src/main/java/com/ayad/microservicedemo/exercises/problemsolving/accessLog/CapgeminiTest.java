package com.ayad.microservicedemo.exercises.problemsolving.accessLog;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CapgeminiTest {


    public static void main(String[] args) {
        CapgeminiTest capgeminiTest = new CapgeminiTest();
        capgeminiTest.checkAccessLogAnomalies();


    }


    public void checkAccessLogAnomalies() {
        Scanner input = new Scanner(System.in);
        int logLength = input.nextInt();
        input.nextLine(); // Consume the remaining newline character
        if (logLength > 0) {
            Map<String, EmployeeStatus> employeeStatusDetails = new HashMap<>(logLength);
            for (int i = 0; i < logLength; i++) {
                String employeeDetails = input.nextLine();
                if (!employeeDetails.isEmpty() && employeeDetails.contains(" ")) {
                    String[] details = employeeDetails.split(" ");
                    String status = details[0];
                    String employeeName = details[1];
                    if (!employeeStatusDetails.containsKey(employeeName) && status.equalsIgnoreCase(EmployeeStatus.ENTRY.getValue())) {//first time employee enter
                        System.out.println(employeeName + " entered");
                        employeeStatusDetails.put(employeeName, EmployeeStatus.ENTRY);

                    } else if (employeeStatusDetails.containsKey(employeeName) &&
                            employeeStatusDetails.get(employeeName).compareTo(EmployeeStatus.ENTRY) == 0
                            && status.equalsIgnoreCase(EmployeeStatus.EXIT.getValue())) {//the employee already entered before and now he is exiting
                        System.out.println(employeeName + " exited");
                        employeeStatusDetails.remove(employeeName);

                    } else if (employeeStatusDetails.containsKey(employeeName) &&
                            employeeStatusDetails.get(employeeName).compareTo(EmployeeStatus.ENTRY) == 0
                            && status.equalsIgnoreCase(EmployeeStatus.ENTRY.getValue())) {// employee trying to enter and he is already entered before
                        System.out.println(employeeName + " entered" + " (ANOMALY)");

                    } else if (!employeeStatusDetails.containsKey(employeeName)
                            && status.equalsIgnoreCase(EmployeeStatus.EXIT.getValue())) {// employee trying to exit and he is not already entered before
                        System.out.println(employeeName + " exited" + " (ANOMALY)");

                    }
                }
            }

        }


    }
}
