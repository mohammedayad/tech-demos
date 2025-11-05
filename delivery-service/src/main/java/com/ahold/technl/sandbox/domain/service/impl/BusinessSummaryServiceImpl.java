package com.ahold.technl.sandbox.domain.service.impl;

import com.ahold.technl.sandbox.domain.model.dtos.BusinessSummaryResponse;
import com.ahold.technl.sandbox.domain.model.entities.Delivery;
import com.ahold.technl.sandbox.domain.repository.ifc.DeliveryRepository;
import com.ahold.technl.sandbox.domain.service.ifc.BusinessSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementation of the BusinessSummaryService interface.
 * Provides business summary metrics.
 */

@Slf4j
@Service
public class BusinessSummaryServiceImpl implements BusinessSummaryService {

    private final DeliveryRepository deliveryRepository;
    private final ZoneId businessZone;

    public BusinessSummaryServiceImpl(DeliveryRepository deliveryRepository, @Value("${app.timezone}") String zone) {
        this.deliveryRepository = deliveryRepository;
        this.businessZone = ZoneId.of(zone);
    }


    /**
     * Generates a summary of business metrics for yesterday.
     *
     * @return BusinessSummaryResponse containing the total number of deliveries and the average time between deliveries in minutes.
     */
    @Override
    public BusinessSummaryResponse yesterdaySummary() {
        log.info("Starting yesterdaySummary calculation");
        ZonedDateTime now = ZonedDateTime.now(businessZone); // now time
        ZonedDateTime startOfToday = now.toLocalDate().atStartOfDay(businessZone); // today 00:00 example 2025-09-15T00:00:00+01:00[Europe/Amsterdam]
        ZonedDateTime startOfYesterday = startOfToday.minusDays(1); // yesterday 00:00 example 2025-09-14T00:00:00+01:00[Europe/Amsterdam]
        log.debug("Calculated time range: startOfYesterday={}, startOfToday={}", startOfYesterday, startOfToday);


        List<Delivery> list = deliveryRepository.findAllByStartedAtBetweenOrderByStartedAtAsc(
                startOfYesterday.toInstant(), startOfToday.toInstant()
        );

        long count = list.size();
        log.info("Number of deliveries found: {}", count);
        long avgMinutes = 0;
        if (count >= 2) {
            long totalMinutes = 0;
            for (int i = 1; i < list.size(); i++) {
                Instant prev = list.get(i - 1).getStartedAt();
                Instant cur = list.get(i).getStartedAt();
                long minutes = Duration.between(prev, cur).toMinutes();
                totalMinutes += minutes;
                log.debug("Calculated minutes between deliveries: prev={}, cur={}, minutes={}", prev, cur, minutes);

            }
            avgMinutes = totalMinutes / (count - 1);
            log.info("Calculated average minutes between deliveries: {}", avgMinutes);

        } else {
            log.warn("Not enough deliveries to calculate average minutes");
        }

        BusinessSummaryResponse summaryResponse = new BusinessSummaryResponse(count, avgMinutes);
        log.info("Generated BusinessSummaryResponse: {}", summaryResponse);
        return summaryResponse;
    }

    @Override
    public void yesterdaySummaryDB() {
        ZonedDateTime now = ZonedDateTime.now(businessZone);
        ZonedDateTime startOfToday = now.toLocalDate().atStartOfDay(businessZone);
        ZonedDateTime startOfYesterday = startOfToday.minusDays(1);
        log.info("Calculated time range: startOfYesterday={}, startOfToday={}", startOfYesterday, startOfToday);
        Object[] queryResult = deliveryRepository.findYestardaySummaryDB(startOfYesterday.toInstant(), startOfToday.toInstant());
        log.info("queryResult: {}", queryResult);
    }
}
