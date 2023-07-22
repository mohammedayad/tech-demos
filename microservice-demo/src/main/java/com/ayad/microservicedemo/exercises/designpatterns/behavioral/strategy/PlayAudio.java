package com.ayad.microservicedemo.exercises.designpatterns.behavioral.strategy;

public class PlayAudio implements PlayBehavior {
    @Override
    public void play() {
        System.out.println("PlayAudio implementation");

    }
}
