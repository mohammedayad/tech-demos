package com.ayad.microservicedemo.exercises.designpatterns.behavioral.state;

public class SwitchOn implements SwitchState {
    @Override
    public void change() {
        System.out.println("SwitchOn");
    }

    @Override
    public void status() {
        System.out.println("Switch is On");

    }

    @Override
    public void complete() {
        System.out.println("SwitchOn completed");

    }
}
