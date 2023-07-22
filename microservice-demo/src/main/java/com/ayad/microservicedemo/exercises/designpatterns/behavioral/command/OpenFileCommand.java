package com.ayad.microservicedemo.exercises.designpatterns.behavioral.command;

public class OpenFileCommand implements Command {
    @Override
    public void execute() {
        System.out.println("OpenFileCommand");
    }
}
