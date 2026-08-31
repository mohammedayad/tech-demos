package com.ayad.microservicedemo.exercises.advancedfeatures.records;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


// note records can only implement interfaces but no extend at all
public record Customer(String name,
                       Integer age,
                       String email,
                       String phoneNumber,
                       LocalDate dateOfBirth,
                       List<UUID> orderIds) {

    private static String address; // Non-record component  so it can't be, instance variable all should be static

    public Customer {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        orderIds = List.copyOf(orderIds); // Ensure immutability of the list

    }

    public Customer(String name,
                    Integer age,
                    String email,
                    String phoneNumber,
                    LocalDate dateOfBirth) {
        this(name, age, email, phoneNumber, dateOfBirth, List.of());
    }


    // instance method
    public String getAddress() {
        return address;
    }

    // static method
    public static void setAddress(String newAddress) {
        address = newAddress;
    }

    public void validateEmail() {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }


    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", orderIds=" + orderIds +
                '}';
    }
}
