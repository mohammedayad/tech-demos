package com.ayad.microservicedemo.exercises.advancedfeatures.sealed.sealedclass;

public abstract sealed class PaymentMethod permits Cash, Card, DigitalWallet {
}
