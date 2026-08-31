package com.ayad.microservicedemo.exercises.advancedfeatures.nestedinneranonymoustypes.staticnested;

import java.time.LocalDate;
import java.util.UUID;

// nested static class builder
public final class Customer {


    public static class Builder {

        private UUID customerID;
        private String firstName;
        private String lastname;
        private LocalDate dateOfBirth;


        public Builder id(UUID id) {
            customerID = id;
            return this;
        }

        public Builder firstName(String fName) {
            firstName = fName;
            return this;
        }


        public Builder lastName(String lName) {
            lastname = lName;
            return this;
        }

        public Builder dateOfBirth(LocalDate date) {
            dateOfBirth = date;
            return this;
        }

        public Customer build() {
            return new Customer(customerID, firstName, lastname, dateOfBirth);
        }

    }


    private final UUID customerID;
    private final String firstName;
    private final String lastname;
    private final LocalDate dateOfBirth;


    private Customer(UUID customerID, String firstName, String lastname, LocalDate dateOfBirth) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;

    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
