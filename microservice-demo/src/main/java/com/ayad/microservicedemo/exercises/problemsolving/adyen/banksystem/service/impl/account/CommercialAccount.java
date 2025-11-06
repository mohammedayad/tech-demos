package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.AccountHolder;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Person;

import java.util.ArrayList;
import java.util.List;

/*
    CommercialAccount represents a bank account for commercial entities.
    It extends the base Account class and includes a list of authorized users
    who can access the account.

 */
public class CommercialAccount extends Account {

    private List<Person> authorizedUsers;

    public CommercialAccount(Long accountNumber, AccountHolder accountHolder, int pin, List<Person> authorizedUsers) {
        super(accountNumber, accountHolder, pin);
        this.authorizedUsers = new ArrayList<>(authorizedUsers);
    }

    // Get the list of authorized users for this commercial account
    public List<Person> getAuthorizedUsers() {
        return authorizedUsers;
    }

    // Add an authorized user to this commercial account
    public void addAuthorizedUser(Person person) {
        this.authorizedUsers.add(person);
    }
}
