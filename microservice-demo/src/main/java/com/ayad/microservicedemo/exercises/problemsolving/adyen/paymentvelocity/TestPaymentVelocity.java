package com.ayad.microservicedemo.exercises.problemsolving.adyen.paymentvelocity;

import java.time.Duration;
import java.util.Scanner;

public class TestPaymentVelocity {

    // sample input:
//    register:1662123600000:c1
//    register:1662123620000:c1
//    register:1662123621000:c2
//    register:1662123630000:c1
//    register:1662123645000:c2
//    get:1662123660000:c1:60
//    get:1662123660000:c1:35
//    get:1662123660000:c1:15
//    get:1662123660000:c3:75

    public static void main(String[] args) {
        VelocityProvider provider = VelocityProvider.getProvider();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) continue;

            String[] parts = line.split(":");
            String command = parts[0];
            if ("register".equals(command)) {
                long timestamp = Long.parseLong(parts[1]);
                String cardId = parts[2];
                provider.registerPayment(new Payment(cardId), timestamp);
            } else if ("get".equals(command)) {
                long timestamp = Long.parseLong(parts[1]);
                String cardId = parts[2];
                Duration duration = Duration.ofSeconds(Long.parseLong(parts[3]));
                int count = provider.getCardUsageCount(new Payment(cardId), duration, timestamp);
                System.out.println(count);
            } else if ("print".equals(command)) {
                provider.printCardUsageMap();

            } else if ("exit".equals(command)) {
                break;
            }

        }
        scanner.close();
    }
}
