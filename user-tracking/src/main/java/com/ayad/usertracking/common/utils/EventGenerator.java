package com.ayad.usertracking.common.utils;


import com.ayad.usertracking.domain.enums.Color;
import com.ayad.usertracking.domain.enums.DesignType;
import com.ayad.usertracking.domain.enums.ProductType;
import com.ayad.usertracking.domain.enums.UserId;
import com.ayad.usertracking.domain.model.Event;
import com.ayad.usertracking.domain.model.Product;
import com.ayad.usertracking.domain.model.User;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;


@Component
public class EventGenerator {

    private Faker faker = new Faker();

    public Event generateEvent() {
        return Event.builder()
                .user(generateRandomUser())
                .product(generateRandomObject())
                .build();
    }

    private User generateRandomUser() {
        return User.builder()
                .userId(faker.options().option(UserId.class))
                .username(faker.name().lastName())
                .dateOfBirth(faker.date().birthday())
                .build();
    }

    private Product generateRandomObject() {
        return Product.builder()
                .color(faker.options().option(Color.class))
                .type(faker.options().option(ProductType.class))
                .designType(faker.options().option(DesignType.class))
                .build();

    }
}
