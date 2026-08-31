package com.ayad.microservicedemo.exercises.advancedfeatures.enums;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PaymentStatus {
    INITIATED("I"),
    CONFIRMED("CO"),
    CANCELED("C"),
    ERROR("E");

    private static final Map<String, PaymentStatus> LOOKUP =
            Arrays.stream(PaymentStatus.values())
                    .collect(Collectors.toMap(PaymentStatus::getValue, Function.identity()));

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentStatus getPaymentStatusFromValue(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching for value: " + value);
    }


    // for large set of values you can implement lookup mechanism
    // rather than looping through all the values as getPaymentStatusFromValue method
    public static PaymentStatus fromValue(String value) {
        return LOOKUP.get(value);
    }
}

class PaymentStatusConverter {

    // is is special for having Enum as keys not work as values
    public static EnumMap<PaymentStatus, String> enumMap = new EnumMap<>(PaymentStatus.class);


    static {
        enumMap.put(PaymentStatus.INITIATED, "I");
        enumMap.put(PaymentStatus.CONFIRMED, "CO");
        enumMap.put(PaymentStatus.CANCELED, "C");
        enumMap.put(PaymentStatus.ERROR, "E");
    }

    public static String toValue(PaymentStatus status) {
        return enumMap.get(status);
    }
}
