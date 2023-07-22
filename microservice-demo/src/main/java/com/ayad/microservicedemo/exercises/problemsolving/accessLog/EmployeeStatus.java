package com.ayad.microservicedemo.exercises.problemsolving.accessLog;

public enum EmployeeStatus {
    ENTRY("entry"),
    EXIT("exit");

    private String value;

    EmployeeStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
