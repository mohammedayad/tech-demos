package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.AccountHolder;

/*
    ConsumerAccount represents a bank account for individual consumers.
    It extends the base Account class without adding any additional properties or methods.
 */
public class ConsumerAccount extends Account{

    // Constructor to initialize a ConsumerAccount with account number, account holder, and PIN
    public ConsumerAccount(Long accountNumber, AccountHolder accountHolder, int pin) {
        super(accountNumber, accountHolder, pin);
    }
}
