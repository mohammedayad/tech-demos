package com.example.testservice.java8.features;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class ZonesAndDates {

    public static void main(String[] args) {
//        for (String zoneId : ZoneId.getAvailableZoneIds()) {
//            ZoneId zoneId1 = ZoneId.of(zoneId);
//            String displayName = zoneId1.getDisplayName(TextStyle.FULL, Locale.US);
//            System.out.println(zoneId1 + " : "+ displayName);
//
//        }

        ZoneId zoneId = ZoneId.of("US/Pacific");
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        ZonedDateTime zonedDateTime2 = ZonedDateTime.now();
        ZonedDateTime zonedDateTime3 = localDateTime.atZone(zoneId);
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTime2);
    }
}
