package com.ayad.microservicedemo.exercises.designpatterns.behavioral.strategy;

public class PlayVideo implements PlayBehavior {
    @Override
    public void play() {
        System.out.println("PlayVideo implementation");
    }
}
