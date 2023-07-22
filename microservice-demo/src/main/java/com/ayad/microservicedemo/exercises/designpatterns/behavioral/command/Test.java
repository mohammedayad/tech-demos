package com.ayad.microservicedemo.exercises.designpatterns.behavioral.command;

public class Test {
    public static void main(String[] args) {
        Invoker invoker=new Invoker(new OpenFileCommand() );
    }
}
