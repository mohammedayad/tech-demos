package com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.ifc;

import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.account.Account;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Company;
import com.ayad.microservicedemo.exercises.problemsolving.adyen.banksystem.service.impl.accountholder.Person;

import java.util.List;

/*
    BankInterface defines the operations that can be performed on the bank system,
    including account retrieval, account creation for consumers and commercial entities,
    and user authentication.
 */
public interface BankInterface {
    Account getAccount(Long accountNumber);

    Long openConsumerAccount(Person person, int pin);

    Long openCommercialAccount(Company company, int pin, List<Person> authorizedUsers);

    boolean authenticateUser(Long accountNumber, int pin);
}
