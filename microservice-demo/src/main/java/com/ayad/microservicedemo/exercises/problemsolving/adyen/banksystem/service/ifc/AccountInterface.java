package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.ifc;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.AccountHolder;

/*
    AccountInterface defines the contract for bank account operations.
    It includes methods to get the account number, retrieve the account holder,
    and validate the PIN associated with the account.
 */
public interface AccountInterface {
    Long getAccountNumber();

    AccountHolder getAccountHolder();

    boolean validatePin(int pin);
}
