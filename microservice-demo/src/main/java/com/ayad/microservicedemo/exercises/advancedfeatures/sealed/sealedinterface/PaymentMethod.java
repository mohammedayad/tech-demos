package com.ayad.microservicedemo.exercises.advancedfeatures.sealed.sealedinterface;

public sealed interface PaymentMethod permits Card, Cash, DigitalWallet {
}
