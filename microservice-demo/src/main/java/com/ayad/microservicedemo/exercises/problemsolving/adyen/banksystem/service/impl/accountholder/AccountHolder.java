package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder;

/*
    AccountHolder is an abstract class representing a generic account holder in the bank system.
    It contains common properties and methods shared by different types of account holders,
    such as individuals and companies.
 */
public abstract class AccountHolder {
    protected String name;

    protected AccountHolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
