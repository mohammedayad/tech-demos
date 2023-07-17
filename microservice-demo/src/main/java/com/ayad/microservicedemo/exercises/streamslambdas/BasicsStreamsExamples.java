package com.ayad.microservicedemo.exercises.streamslambdas;

import com.ayad.microservicedemo.exercises.model.Category;
import com.ayad.microservicedemo.exercises.model.ExampleData;
import com.ayad.microservicedemo.exercises.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BasicsStreamsExamples {


    public static void main(String[] args) {
        //howToCreateStreams();
        List<Product> products = ExampleData.getProducts();
        //products.stream().filter(product -> product.getCategory().equals(Category.FOOD)).forEach(System.out::println);
        // products.stream().map(product -> product.getName()).forEach(System.out::println);
        //products.stream().flatMap(product -> Stream.ofNullable(product.getName())).forEach(System.out::println);
        Optional<Product> product = products.stream()
                .filter(p -> p.getCategory().equals(Category.FOOD))
                .findAny();//findFirst();
        product.ifPresentOrElse(product1 -> System.out.println(product1.getName()), () -> System.out.println("not found"));

        boolean anyMatch = products.stream().anyMatch(p -> p.getCategory().equals(Category.FOOD));
        System.out.println("anyMatch " + anyMatch);

        boolean allMatch =products.stream().allMatch(product1 -> product1.getPrice().compareTo(new BigDecimal(0)) > 0);
        System.out.println("allMatch " + allMatch);

        boolean noneMatch =products.stream().noneMatch(product1 -> product1.getPrice().compareTo(new BigDecimal(0)) > 0);
        System.out.println("noneMatch " + noneMatch);

        //collecting
        List<String> collect=products.stream().filter(product1 -> product1.getCategory().equals(Category.FOOD))
                .map(Product::getName)
                .collect(Collectors.toList());
        System.out.println(collect);

        String collect1= products.stream().map(Product::getCategory).distinct().map(Category::name)
                .collect(Collectors.joining(":"));
        System.out.println(collect1);



    }

    private static void howToCreateStreams() {
        // create different streams
        List<Product> products = ExampleData.getProducts();
        Stream<Product> stream1 = products.stream();


        Stream<String> stream2 = Arrays.stream(new String[]{"1", "2", "3"});

        IntStream stream3 = Arrays.stream(new int[]{1, 2, 3});


        Stream<String> stream4 = Stream.of("1", "2", "3");

        Stream<String> stream5 = Stream.ofNullable("1");

        Stream<?> stream6 = Stream.empty();
        //start is inclusive end is exclusive
        IntStream dice = new Random().ints(1, 7);


        try (BufferedReader in = new BufferedReader(new FileReader("", StandardCharsets.UTF_8))) {
            in.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
