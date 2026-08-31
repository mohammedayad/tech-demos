package com.ayad.microservicedemo.exercises.advancedfeatures.examples.onlineshopping;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderService {


    // order contains list of OrderItem
    // it can be standard productItem or DiscountItem
    // which it is discount coupon need to be subtracted from total price
    // so will need to loop over the items check if it is standard item will get the price
    // and multiply it to quantity and if it is discount will just subtract it


    // pattern matching with records and sealed
    // also one note this style called data oriented programming as if we followed OOP
    // we will add the behaviour (logic) with the data holder like states (attributes) and behaviour (methods)
    // for example we will have a method calculate inside interface OrderItem and override it in
    // ProductItem and DiscountItem but we have separated the behaviour from the data holder object
    // there is no good and bad behaviour between data oriented and OOP
    public static BigDecimal calculateOrderPrice(Order order) {
        return order.items()
                .stream()
                .map(orderItem -> switch (orderItem) {
                            // Unnamed patterns and variables are not supported at language level '21'
//                    case ProductItem(_,_,int quantity,BigDecimal price) -> price.multiply(BigDecimal.valueOf(quantity));
//                    case DiscountItem(_,_,BigDecimal discount) ->
                            case ProductItem(String productCode, String description, int quantity, BigDecimal price) ->
                                    price.multiply(BigDecimal.valueOf(quantity));
                            case DiscountItem(String discountCode, String description, BigDecimal discountAmount) ->
                                    discountAmount.negate();

                        }
                ).reduce(BigDecimal.ZERO, BigDecimal::add);


    }

    public static void main(String[] args) {
        UUID customerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        List<OrderItem> items = List.of(
                new ProductItem("123", "Pencil", 3, new BigDecimal("0.79")),
                new ProductItem("1234", "Notebook", 1, new BigDecimal("3.99")),
                new DiscountItem("HOLIDAY2026", "Holidy Discount", new BigDecimal("2.00"))
        );
        Order order = new Order(customerId, orderId, items);
        BigDecimal orderAmount = calculateOrderPrice(order);
        System.out.println("orderAmount: " + orderAmount);

    }
}
