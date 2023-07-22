package com.ayad.microservicedemo.exercises.designpatterns.behavioral.strategy;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class MediaFile {
    private PlayBehavior behavior;

    public void play() {
        if ((behavior != null)) {
            behavior.play();
        } else {
            System.out.println("play behaviour not supported");
        }
    }

}
