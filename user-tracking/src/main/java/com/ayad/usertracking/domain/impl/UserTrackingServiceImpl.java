package com.ayad.usertracking.domain.impl;

import com.ayad.usertracking.clients.event.ifc.UserTrackingProducer;
import com.ayad.usertracking.common.utils.EventGenerator;
import com.ayad.usertracking.domain.enums.UserId;
import com.ayad.usertracking.domain.ifc.UserTrackingService;
import com.ayad.usertracking.domain.model.Event;
import com.ayad.usertracking.domain.model.PreferredProduct;
import com.ayad.usertracking.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class UserTrackingServiceImpl implements UserTrackingService {

    private final EventGenerator eventGenerator;
    private final UserTrackingProducer userTrackingProducer;

    public UserTrackingServiceImpl(EventGenerator eventGenerator, UserTrackingProducer userTrackingProducer) {
        this.eventGenerator = eventGenerator;
        this.userTrackingProducer = userTrackingProducer;
    }

    @Override
    public void produceEvents() {
        for (int i = 1; i <= 10; i++) {
            log.info("Generating event {}", i);

            Event event = eventGenerator.generateEvent();

            String key = extractKey(event);
            String value = extractValue(event);

            log.info("Producing to Kafka the record:{} : {} ", key, value);
            userTrackingProducer.publish(key, value);

        }

    }

    @Override
    public void processSuggestions(String userId, String product) {
        String[] valueSplit = product.split(",");
        String productType = valueSplit[0];
        String productColor = valueSplit[1];
        String productDesign = valueSplit[2];
        log.info("User with ID: {} showed interest over {} of color {} and design {} ",
                userId, productType, productColor, productDesign);
        // Retrieve preferences from Database
        User user = new User(UserId.valueOf(userId));

        // Update user preferences
        user.getPreferences()
                .add(new PreferredProduct(productColor, productType, productDesign));

        // Generate list of suggestions
        user.setSuggestions(generateSugestions(user.getPreferences()));

    }

    private static String extractKey(Event event) {
        return event.getUser().getUserId().toString();
    }

    private static String extractValue(Event event) {
        return String.format("%s,%s,%s", event.getProduct().getType(), event.getProduct().getColor(), event.getProduct().getDesignType());
    }

    /**
     * @return hardcoded suggestions
     */
    private List<String> generateSugestions(List<PreferredProduct> preferences) {
        return Arrays.asList("TSHIRT,BLUE",
                "DESIGN,ORANGE,ROCKET",
                "TSHIRT,PURPLE,CAR");
    }
}
