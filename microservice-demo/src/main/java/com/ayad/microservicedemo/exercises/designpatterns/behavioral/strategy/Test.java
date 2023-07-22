package com.ayad.microservicedemo.exercises.designpatterns.behavioral.strategy;

public class Test {

    public static void main(String[] args) {
        MediaFile mediaFile=new MediaFile(new PlayVideo());
        mediaFile.play();
        mediaFile.setBehavior(new PlayAudio());
        mediaFile.play();
        mediaFile.setBehavior(new PlayAnimation());
        mediaFile.play();
    }
}
