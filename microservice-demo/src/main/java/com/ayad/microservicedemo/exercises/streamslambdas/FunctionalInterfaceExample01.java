package com.ayad.microservicedemo.exercises.streamslambdas;

import com.ayad.microservicedemo.exercises.model.Category;
import com.ayad.microservicedemo.exercises.model.ExampleData;
import com.ayad.microservicedemo.exercises.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionalInterfaceExample01 {


    public static void main(String[] args) {
        List<Product> products = ExampleData.getProducts();

        // Function interface
        Function<Product, BigDecimal> productToPrice = Product::getPrice;
        Function<BigDecimal, String> priceFormat = price -> "price is " + price;
        // Function <Product, String> productToMessage=productToPrice.andThen(priceFormat);
        Function<Product, String> productToMessage = priceFormat.compose(productToPrice);

        // Predicate interface
        Predicate<Product> isFood = product -> product.getCategory() == Category.FOOD;
        Predicate<Product> isCheap = product -> product.getPrice().compareTo(new BigDecimal(2.00)) < 0;

        findProduct(products, p -> p.getName().equals("Spaghetti")).map(productToMessage)
                .ifPresentOrElse(price -> System.out.println(price), () -> System.out.println("not found"));

        findProduct(products, p -> p.getName().equals("Spaghetti")).map(Product::getPrice)
                .ifPresentOrElse(price -> System.out.println(price), () -> System.out.println("not found"));

        findProduct(products, isFood.and(isCheap)).map(Product::getPrice)
                .ifPresentOrElse(price -> System.out.println(price), () -> System.out.println("not found"));





        // createMapOfProductsCategory(products);


    }

    private static void createMapOfProductsCategory(List<Product> products) {
        Map<Category, List<Product>> categoryProductMap = new HashMap<>();
        for (Product product : products) {
            /*
            if (!categoryProductMap.containsKey(product.getCategory())) {
                categoryProductMap.put(product.getCategory(), new ArrayList<>());
            }
            categoryProductMap.get(product.getCategory()).add(product);

             */
            categoryProductMap.computeIfAbsent(product.getCategory(), category -> new ArrayList<>()).add(product);

        }
        categoryProductMap.forEach((key, value) -> {
            System.out.println("Category: " + key);
            value.forEach(product -> System.out.println(product.getName()));
        });


    }

    private static Optional<Product> findProduct(List<Product> products, Predicate<Product> predicate) {
        for (Product product : products) {
            if (predicate.test(product)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();

    }
}
