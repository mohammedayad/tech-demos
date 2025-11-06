package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.executortest;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Company;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Person;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.bank.Bank;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.transaction.Transaction;

import java.util.List;

public class ExecutorTest {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Create account holders
        Person john = new Person("John Doe");
        Company acme = new Company("Acme Corp");
        Person manager = new Person("Jane Manager");

        // Open accounts
        Long johnAcc = bank.openConsumerAccount(john, 1234);
        Long acmeAcc = bank.openCommercialAccount(acme, 5678, List.of(manager));

        // Deposit some money
        bank.getAccount(johnAcc).deposit(1000);
        bank.getAccount(acmeAcc).deposit(5000);

        // Perform transaction
        Transaction transaction = new Transaction(bank, johnAcc, acmeAcc, 200);
        transaction.execute();

        // Show balances
        System.out.println("John's balance: " + bank.getAccount(johnAcc).getBalance());
        System.out.println("Acme's balance: " + bank.getAccount(acmeAcc).getBalance());

    }
}
