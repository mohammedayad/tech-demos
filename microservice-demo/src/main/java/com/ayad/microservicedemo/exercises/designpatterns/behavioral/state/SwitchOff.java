package com.ayad.microservicedemo.exercises.designpatterns.behavioral.state;

public class SwitchOff implements SwitchState {
    @Override
    public void change() {
        System.out.println("SwitchOff");
    }

    @Override
    public void status() {
        System.out.println("Switch is Off");

    }

    @Override
    public void complete() {
        System.out.println("SwitchOff completed");

    }
}
