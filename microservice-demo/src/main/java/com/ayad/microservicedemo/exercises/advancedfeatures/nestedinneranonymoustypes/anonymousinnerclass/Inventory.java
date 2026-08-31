package com.ayad.microservicedemo.exercises.advancedfeatures.nestedinneranonymoustypes.anonymousinnerclass;


import java.util.HashMap;
import java.util.Map;

// inventory service will have the products and quantity of these products
// if a user add some product to their shopping card, check quantity exist if no throw exception
// if exist will reserve this quantity so will have to keep track of available and reserved
// if customer checkout and confirm will decrease this amount from available and remove reserved
// if customer changed his mind will just remove the reserved
public class Inventory {

    // save product code and the quantity
    private final Map<String, Integer> available;
    private final Map<String, Integer> reserved;

    public Inventory() {
        this.available = new HashMap<>();
        this.reserved = new HashMap<>();
    }


    public void add(String productCode, int quantity) {
        available.merge(productCode, quantity, Integer::sum);
    }


    public Reservation reserve(String productCode, int quantity) {
        int stock = available.getOrDefault(productCode, 0);
        if (stock < quantity) {
            throw new IllegalStateException("Product out of stock");
        }
        available.put(productCode, stock - quantity);
        reserved.merge(productCode, quantity, Integer::sum);
        return new Reservation() {

            private boolean isActive = true;


            @Override
            public void release() {

                if (!isActive) {
                    throw new IllegalStateException("inactive reservation can't be release");
                }
                available.merge(productCode, quantity, Integer::sum);
                reserved.merge(productCode, -quantity, Integer::sum);
                isActive = false;

            }

            @Override
            public void commit() {

                if (!isActive) {
                    throw new IllegalStateException("inactive reservation can't be committed");
                }
                reserved.merge(productCode, -quantity, Integer::sum);
                isActive = false;

            }
        };
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "available=" + available +
                ", reserved=" + reserved +
                '}';
    }
}
