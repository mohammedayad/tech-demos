package com.ayad.microservicedemo.exercises.designpatterns.behavioral.strategy;

public class PlayAnimation implements PlayBehavior {
    @Override
    public void play() {
        System.out.println("PlayAnimation implementation");

    }
}
