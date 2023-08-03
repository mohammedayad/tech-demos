package com.ayad.microservicedemo.exercises.problemsolving.sitefutureskill;

import java.util.*;
import java.util.stream.Collectors;


//import api.*;


//public class Solution implements SolutionInterface {
public class Solution {
    //private APICaller api;
    private Map<Integer, Integer> dayPrices;
    private List<Map.Entry<String, Integer>> sortedEntries;
    int buyDay, sellDay;

//    public Solution(APICaller api) {
//        this.api = api;
//        System.out.println("Press run code to see this in the console!");
//        // You can initiate and calculate things here
//        dayPrices = new TreeMap<>();
//    }

    /**
     * Return the day which you buy silver. The first day has number zero.
     * This method is called first, and only once.
     */
    public int getBuyDay() {
        // Write your code here
        int NoOfDays = 10; //api.getNumDays();
        System.out.println("NoOfDays: " + NoOfDays);
        int minBuyValue = Integer.MAX_VALUE;
        int buyDay = 0;
        for (int i = 0; i < NoOfDays; i++) {
            //int dayPrice = api.getPriceOnDay(i);
            int dayPrice = 0;

            dayPrices.put(i, dayPrice);

        }
        handleDayPrices();
        if (!sortedEntries.isEmpty()) {
            String maxProfit = sortedEntries.get(0).getKey();
            String[] parts = maxProfit.split(":");
            if (parts.length == 2) {
                buyDay = Integer.parseInt(parts[0]);
                sellDay = Integer.parseInt(parts[1]);
                System.out.println("maxProfit days: " + maxProfit);
                System.out.println("buyDay: " + buyDay);
                System.out.println("sellDay: " + sellDay);
            } else {
                System.out.println("Invalid key format.");
            }
        } else {
            System.out.println("No entries in sorted list.");
        }

        return buyDay;
    }


    private void handleDayPrices() {
        Map<String, Integer> priceDifferences = dayPrices.keySet().stream()
                .flatMap(day1 -> dayPrices.keySet().stream()
                        .filter(day2 -> day2 > day1)
                        .map(day2 -> {
                            int price1 = dayPrices.get(day1);
                            int price2 = dayPrices.get(day2);
                            int difference = price2 - price1;
                            String key = day1 + ":" + day2;
                            return new AbstractMap.SimpleEntry<>(key, difference);
                        }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("priceDifferences: " + priceDifferences);

        sortedEntries = priceDifferences.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Integer>>comparingInt(entry -> entry.getValue()).reversed())
                .collect(Collectors.toList());
        System.out.println("sortedEntries: " + sortedEntries);
    }


    private void handleDayPrices2() {
        Map<String, Integer> priceDifferences = new HashMap<>();
        List<Integer> days = new ArrayList<>(dayPrices.keySet());
        for (int i = 0; i < days.size() - 1; i++) {
            int day1 = days.get(i);
            int price1 = dayPrices.get(day1);
            for (int j = i + 1; j < days.size(); j++) {
                int day2 = days.get(j);
                int price2 = dayPrices.get(day2);
                int difference = price2 - price1;
                String key = day1 + ":" + day2;
                priceDifferences.put(key, difference);
            }
        }
        System.out.println("priceDifferences: " + priceDifferences);
        sortedEntries = new ArrayList<>(priceDifferences.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        System.out.println("sortedEntries: " + sortedEntries);
    }


    /**
     * Return the day to sell silver on. This day has to be after (greater
     * than) the buy day. The first day has number zero (although this is not
     * a valid sell day). This method is called second, and only once.
     */
    public int getSellDay() {
        // Write your code here

        return sellDay;
    }
}
