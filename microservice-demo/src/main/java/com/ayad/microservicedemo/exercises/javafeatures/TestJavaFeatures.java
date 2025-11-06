package com.ayad.microservicedemo.exercises.javafeatures;

public class TestJavaFeatures {


    public static void main(String[] args) {
        System.out.println(5/2);
        Account account = new Account(1,10,"active account",100);
        System.out.println( account );
        int arr[] = {10,11,12,14};
        account.printVariableParameters(arr); // instance method
        Account.print(10,1,2,3,4); // class method

        System.out.println(Account.text);
        Account.text2 ="""
            [
                {
                    "id" : 0000,
                    "customerId" : 1
                }
            ]
            """;
        System.out.println(Account.text2);
    }
}
