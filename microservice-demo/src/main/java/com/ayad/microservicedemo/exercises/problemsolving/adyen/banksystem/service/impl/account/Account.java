package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.ifc.AccountInterface;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.AccountHolder;

/*
    Abstract class Account implements the AccountInterface.
    It provides common properties and methods for different types of bank accounts,
    such as account number, PIN validation, account holder information, balance management,
    deposit, and withdrawal functionalities.
 */
public abstract class Account implements AccountInterface {

    protected Long accountNumber;
    protected int pin;
    protected AccountHolder accountHolder;
    protected double balance;

    protected Account(Long accountNumber, AccountHolder accountHolder, int pin) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.pin = pin;
        this.balance = 0.0;
    }


    // Get the account number
    @Override
    public Long getAccountNumber() {
        return accountNumber;
    }

    // Get the account holder associated with this account
    @Override
    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    // Validate the provided PIN against the account's PIN
    @Override
    public boolean validatePin(int pin) {
        return this.pin == pin;
    }

    // Get the current balance of the account
    public double getBalance() {
        return balance;
    }

    // Deposit a specified amount into the account
    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    // Withdraw a specified amount from the account if sufficient balance exists
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
