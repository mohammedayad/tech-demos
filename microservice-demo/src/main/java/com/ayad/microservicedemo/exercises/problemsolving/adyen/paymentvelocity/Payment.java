package com.ayad.microservicedemo.exercises.problemsolving.adyen.paymentvelocity;

/*
    Payment class represents a payment transaction with a card token.
    It encapsulates the card token used for the payment.
 */
public class Payment {
    private String cardToken;

    public Payment(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardToken() {
        return cardToken;
    }
}
