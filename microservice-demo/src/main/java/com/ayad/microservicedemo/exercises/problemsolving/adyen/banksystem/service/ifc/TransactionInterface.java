package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.ifc;

/*
    TransactionInterface defines the contract for executing a transaction.
    Any class implementing this interface must provide an implementation for the execute method.
 */
public interface TransactionInterface {
    void execute();
}
