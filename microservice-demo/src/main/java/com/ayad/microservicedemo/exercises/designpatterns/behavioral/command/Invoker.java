package com.ayad.microservicedemo.exercises.designpatterns.behavioral.command;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class Invoker {
    private Command command;

}
