package com.ayad.microservicedemo.exercises.advancedfeatures.nestedinneranonymoustypes;


import com.ayad.microservicedemo.exercises.advancedfeatures.nestedinneranonymoustypes.innerclass.Inventory;
import com.ayad.microservicedemo.exercises.advancedfeatures.nestedinneranonymoustypes.staticnested.Customer;

import java.time.LocalDate;
import java.util.UUID;

public class Test {


    public static void main(String[] args) {
        Customer customer = new Customer.Builder()
                .id(UUID.randomUUID())
                .firstName("Mohammed")
                .lastName("Ayad")
                .dateOfBirth(LocalDate.now())
                .build();
        System.out.println(customer);


        // test inventory for non-static inner class
        Inventory inventory = new Inventory();
        inventory.add("ABC123", 3);
        inventory.add("ABC1234", 2);

        Inventory.Reservation reserve = inventory.reserve("ABC123", 2);
        System.out.println("inventory " + inventory);
        System.out.println("reserve " + reserve);
//        reserve.commit();
//        System.out.println("inventory after committing " + inventory);
//        System.out.println("reserve after committing " + reserve);
        reserve.release();
        System.out.println("inventory after releasing " + inventory);
        System.out.println("reserve after releasing " + reserve);

        // you can creat instance of the inner non-static class like the below but the best practise to make
        // reservation with private constructor to not allow external access
        // also please not inner class is related to instance (this) so maybe cause problem in serialization or nested beans
        // so it might better to use static nested class instead
        Inventory.Reservation reservation = inventory.new Reservation("ABC", 3);
        Inventory.Reservation reservation2 = new Inventory().new Reservation("ABC", 3);
    }

}
