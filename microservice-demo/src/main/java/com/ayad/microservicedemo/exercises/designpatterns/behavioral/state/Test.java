package com.ayad.microservicedemo.exercises.designpatterns.behavioral.state;

public class Test {

    public static void main(String[] args) {
        Switch aSwitch = new Switch(new SwitchOn());
        aSwitch.change();
        aSwitch.setState(new SwitchOff());
        aSwitch.change();
    }
}
