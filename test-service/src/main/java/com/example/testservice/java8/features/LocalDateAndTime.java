package com.example.testservice.java8.features;

import java.time.*;

public class LocalDateAndTime {

    public static void main(String[] args) {


        // LocalDate
        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.of(2020, 12, 1);
        LocalDate ld3 = LocalDate.of(2020, Month.DECEMBER, 1);

        // LocalTime
        LocalTime lt1 = LocalTime.now();
        LocalTime lt2 = LocalTime.of(10, 12);

        // LocalDateTime
        LocalDateTime ldt1 = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.
                of(2022, 12, 2, 22, 23, 10);

        LocalDateTime ldt3 = LocalDateTime.of(ld1, lt1);

        // MonthDay
        MonthDay md1 = MonthDay.now();
        MonthDay md2 = MonthDay.of(12, 31);
        md2.atYear(1992);

        // YearMonth

        YearMonth ym1 = YearMonth.now();
        YearMonth ym2 = YearMonth.of(1992, 12);
        ym2.atDay(12);
    }


}
