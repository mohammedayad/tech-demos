package com.ayad.microservicedemo.exercises.streamslambdas;

import com.ayad.microservicedemo.exercises.model.Category;
import com.ayad.microservicedemo.exercises.model.ExampleData;
import com.ayad.microservicedemo.exercises.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdvancedStreamsExamples {

    public static void main(String[] args) {
        List<Product> products = ExampleData.getProducts();
        Optional<BigDecimal> totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce((result, price) -> result.add(price));
        System.out.println("Optional<T> reduce(BinaryOperator<T> var1)");
        totalPrice.ifPresentOrElse(bigDecimal -> System.out.println(bigDecimal), () -> System.out.println("not found"));
        Optional<BigDecimal> totalPrice1 = products.stream().map(Product::getPrice).reduce(BigDecimal::add);
        totalPrice1.ifPresentOrElse(bigDecimal -> System.out.println(bigDecimal), () -> System.out.println("not found"));
        System.out.println("T reduce(T var1, BinaryOperator<T> var2)");

        BigDecimal totalPrice2 = products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(totalPrice2);

        System.out.println("<U> U reduce(U var1, BiFunction<U, ? super T, U> var2, BinaryOperator<U> var3)");
        BigDecimal totalPrice3 = products.stream()
                .reduce(BigDecimal.ZERO, (result, product) -> result.add(product.getPrice()), BigDecimal::add);
        System.out.println(totalPrice3);

        //compare between Collect and Reduce
        //use Reduce for return a list of names of products
        List<String> names = products.stream().reduce(new ArrayList<String>(), (strings, product) -> {
            ArrayList<String> newArray = new ArrayList<>(strings);
            newArray.add(product.getName());
            return newArray;
        }, (list1, list12) -> {
            ArrayList<String> newArray = new ArrayList<>(list1);
            newArray.addAll(list12);
            return newArray;

        });
        System.out.println(names);

        List<String> names2 = products.stream().reduce(new ArrayList<String>(), (list, product) -> {
            System.out.println("current list: " + list + " current product name: " + product.getName());
            list.add(product.getName());
            return list;
        }, (list1, list2) -> {
            System.err.println("list1: " + list1 + " list2: " + list2);
            list1.addAll(list2);
            return list1;
        });
        System.out.println(names2);


        //using collect
        List<String> names3 = products.stream().collect(ArrayList::new, (list, product) -> list.add(product.getName()), (list1, list2) -> list1.addAll(list2));
        List<String> names4 = products.stream().collect(ArrayList::new, (list, product) -> list.add(product.getName()), List::addAll);
        System.out.println("collect: " +names4);

        //Collectors class list of category and products names
        Map<Category, BigDecimal> map = products.stream().collect(Collectors.toMap(Product::getCategory, Product::getPrice, BigDecimal::add));
        System.out.println("Collectors: " +map);

        // using Grouping
        Map<Category, List<Product>> categoryListMap = products.stream().collect(Collectors.groupingBy(Product::getCategory));
        System.out.println(categoryListMap);


        //list of category and products names
        Map<Category, List<String>> listMap = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(Product::getName, Collectors.toList())));
        System.out.println(listMap);

        //list of category and total price of products
        Map<Category, BigDecimal> map2 = products.stream().collect(Collectors.groupingBy(Product::getCategory,
                Collectors.mapping(Product::getPrice, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        System.out.println(map2);


        //Partitioning

        Predicate<Product> priceLimit = product -> product.getPrice().compareTo(new BigDecimal(5)) < 0;

        Map<Boolean, List<Product>> booleanListMap = products.stream().collect(Collectors.partitioningBy(priceLimit));
        System.out.println("product prices smaller than 5");
        booleanListMap.get(true).forEach(System.out::println);
        System.out.println("product prices larger than 5");
        booleanListMap.get(false).forEach(System.out::println);

        System.out.println(getSortedProductNames(ExampleData.getProducts()));


    }

    private void createStreams() {
        Stream<int[]> ints = Stream.of(new int[]{1, 2, 3});
        // Optional<int[]> x=ints.findFirst();
        // System.out.println(x.get()[0]);
        // Optional<int[]> y=ints.map(ints1 -> ints1).findFirst();
        // System.out.println(y.get()[0]);
        Stream<Integer> ints2 = Stream.of(1, 2, 3, 4);
        IntStream stream3 = Arrays.stream(new int[]{1, 2, 3});
        Stream<UUID> uuidStream = Stream.generate(() -> UUID.randomUUID());
        uuidStream.limit(10).forEach(System.out::println);
        Stream<BigInteger> iterate = Stream.iterate(BigInteger.ONE, bigInteger -> bigInteger.multiply(BigInteger.TWO));
        iterate.limit(10).forEach(System.out::println);

        Stream<Character> iterate2 = Stream.iterate('A', character -> character <= 'Z', character -> (char) (character + 1));
        iterate2.forEach(System.out::println);

        Stream.Builder<String> builder = Stream.builder();
        builder.add("one");
        builder.add("two");
        builder.add("three");
        Stream<String> stream = builder.build();
        stream.forEach(System.out::println);

    }

    /**
     * Exercise 1: Collect product names into a sorted set.
     *
     * @param products A list of products.
     * @return A TreeSet containing the names of the products.
     */
    public static Set<String> getSortedProductNames(List<Product> products) {
        // TODO: Collect the names of the products into a TreeSet.
        //
        // Hint: Use the collect() method that takes three functions parameters.
        // What is the purpose of each of these three functions and how do you implement them? (Consult the API documentation).
        // Use method references or lambda expressions to implement the three functions.

        TreeSet<String> sortedNames = products.stream()
                .collect(TreeSet::new, (treeSet, product) -> treeSet.add(product.getName()), (treeSet1, treeSet2) -> treeSet1.addAll(treeSet2));
        return sortedNames;

    }
}
