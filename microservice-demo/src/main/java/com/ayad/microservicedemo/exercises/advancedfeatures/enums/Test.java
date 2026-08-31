package com.ayad.microservicedemo.exercises.advancedfeatures.enums;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;

public class Test {

    public static void main(String[] args) {
        PaymentStatus paymentStatus = PaymentStatus.valueOf("CONFIRMED");
        System.out.println("paymentStatus: " + paymentStatus.name() + " value: " + paymentStatus.getValue());
//        PaymentStatus paymentStatus2 = PaymentStatus.valueOf("C"); // can't get payment status with value need custom handle
        PaymentStatus paymentStatus2 = PaymentStatus.getPaymentStatusFromValue("C");
        System.out.println("paymentStatus2: " + paymentStatus2.name() + " value: " + paymentStatus2.getValue());

        System.out.println(MathOperationsWithSingleInstanceMethod.ADD.calculate(10, 5));

        MathOperationsWithOopPolymorphism subtract = MathOperationsWithOopPolymorphism.SUBTRACT;
        System.out.println(subtract.calculate(10, 5));

        MathOperationsWithOopPolymorphismWithInterface multiply = MathOperationsWithOopPolymorphismWithInterface.MULTIPLY;
        System.out.println(multiply.calculate(10, 5));


        MathOperationsWithLambda add = MathOperationsWithLambda.ADD;
        System.out.println(add.calculate(10, 5));

        // EnumSet and EnumMap
        EnumSet<DayOfWeek> days = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.TUESDAY);

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        System.out.println(days.contains(today));

        System.out.println(PaymentStatusConverter.toValue(PaymentStatus.ERROR));


    }
}
