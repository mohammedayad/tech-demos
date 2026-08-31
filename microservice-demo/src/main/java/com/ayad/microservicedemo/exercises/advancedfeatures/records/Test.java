package com.ayad.microservicedemo.exercises.advancedfeatures.records;

import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {
        Customer customer = new Customer(
                "Mohammed",
                33,
                "test@ggmail.com",
                "123456789",
                LocalDate.of(1990, 1, 1));
        System.out.println(customer.email());
        System.out.println(customer.toString());
        customer.validateEmail();
        System.out.println(customer.getAddress());
        Customer.setAddress("123 Main St, Anytown, USA");
        System.out.println(customer.getAddress());
    }
}
