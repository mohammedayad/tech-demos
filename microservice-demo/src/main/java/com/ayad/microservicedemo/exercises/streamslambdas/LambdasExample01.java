package com.ayad.microservicedemo.exercises.streamslambdas;

import com.ayad.microservicedemo.exercises.model.ExampleData;
import com.ayad.microservicedemo.exercises.model.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class LambdasExample01 {


    public static void main(String[] args) {
        // Interface Comparator implemented with an anonymous class.
        System.out.println("-----------------------------------------anonymous class implementation---------------------------------------------------------------");
        List<Product> products = ExampleData.getProducts();
        products.sort(new Comparator<Product>() {

            @Override
            public int compare(Product p1, Product p2) {
                return p1.getPrice().compareTo(p2.getPrice());
            }
        });
        print(products);
        System.out.println("--------------------------------------------------------------------------------------------------------");

        System.out.println("-----------------------------------------lambda Comparator implementation---------------------------------------------------------------");
        products.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
        print(products);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------lambda Comparator internal functions implementation---------------------------------------------------------------");
        products.sort(Comparator.comparing(Product::getPrice).thenComparing(Product::getName).reversed());
        print(products);




        // log.info("Products: {}", products);

    }

    private static void print(List<?> items) {

        items.forEach(System.out::println);


    }
}
