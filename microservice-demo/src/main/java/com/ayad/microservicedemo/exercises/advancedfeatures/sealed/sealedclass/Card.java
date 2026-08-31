package com.ayad.microservicedemo.exercises.advancedfeatures.sealed.sealedclass;

public abstract sealed class Card extends PaymentMethod permits CreditCard, DebitCard {
}
