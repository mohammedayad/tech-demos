package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.bank;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.ifc.BankInterface;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account.Account;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account.CommercialAccount;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account.ConsumerAccount;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Company;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Person;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    Bank class implements the BankInterface and provides functionalities to manage bank accounts,
    including opening consumer and commercial accounts, retrieving accounts, and authenticating users.
 */
public class Bank implements BankInterface {

    private Map<Long, Account> accounts;
    private long nextAccountNumber;

    public Bank() {
        this.accounts = new ConcurrentHashMap<>();
        this.nextAccountNumber = 1000L;
    }

    // Retrieve an account by its account number
    @Override
    public Account getAccount(Long accountNumber) {
        return accounts.get(accountNumber);
    }

    // Open a new consumer account for a person with a specified PIN
    @Override
    public Long openConsumerAccount(Person person, int pin) {
        Long accountNumber = nextAccountNumber++;
        Account account = new ConsumerAccount(accountNumber, person, pin);
        accounts.put(accountNumber, account);
        return accountNumber;
    }

    // Open a new commercial account for a company with a specified PIN and authorized users
    @Override
    public Long openCommercialAccount(Company company, int pin, List<Person> authorizedUsers) {
        Long accountNumber = nextAccountNumber++;
        Account account = new CommercialAccount(accountNumber, company, pin, authorizedUsers);
        accounts.put(accountNumber, account);
        return accountNumber;
    }

    // Authenticate a user by verifying the account number and PIN
    @Override
    public boolean authenticateUser(Long accountNumber, int pin) {
        Account account = accounts.get(accountNumber);
        return account != null && account.validatePin(pin);
    }
}
