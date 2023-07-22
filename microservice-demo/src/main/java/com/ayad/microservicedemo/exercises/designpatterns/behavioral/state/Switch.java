package com.ayad.microservicedemo.exercises.designpatterns.behavioral.state;

import lombok.Setter;

@Setter
public class Switch {
    private SwitchState state;

    public Switch(SwitchState state) {

        this.state = state;
    }

    public void change() {
        state.change();
    }

    public void status() {
        state.status();
    }

    public void complete() {
        state.complete();
    }

}
