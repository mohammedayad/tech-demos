package com.ayad.microservicedemo.test;

public interface TestInterface {

    public static final String STATIC_VARIABLE = "static variable";
    public String INSTANCE_VARIABLE = "instance variable";

    public static void method() {

    }

    public void method2();


    public default void method3() {

    }
}
