package com.ayad.microservicedemo.exercises.designpatterns.behavioral.command;

public class SaveFileCommand implements Command {
    @Override
    public void execute() {
        System.out.println("SaveFileCommand");
    }
}
