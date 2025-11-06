package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.transaction;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.ifc.TransactionInterface;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account.Account;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.bank.Bank;

/*
    Transaction class implements the TransactionInterface to perform fund transfers
    between two bank accounts. It checks for sufficient funds before executing the transfer.
 */
public class Transaction implements TransactionInterface {
    private Bank bank;
    private Long fromAccount;
    private Long toAccount;
    private double amount;

    public Transaction(Bank bank, Long fromAccount, Long toAccount, double amount) {
        this.bank = bank;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    /*
        Executes the transaction by transferring funds from one account to another.
        It first retrieves the accounts from the bank, checks for their validity,
        and then attempts to withdraw the specified amount from the source account.
        If successful, it deposits the amount into the destination account.

     */
    @Override
    public void execute() {
        Account from = bank.getAccount(fromAccount);
        Account to = bank.getAccount(toAccount);

        if (from == null || to == null) {
            System.out.println("Invalid account(s).");
            return;
        }

        if (from.withdraw(amount)) {
            to.deposit(amount);
            System.out.printf("Transaction successful: %.2f transferred from %s to %s%n",
                    amount,
                    from.getAccountHolder().getName(),
                    to.getAccountHolder().getName());
        } else {
            System.out.println("Transaction failed: insufficient funds.");
        }
    }
}
