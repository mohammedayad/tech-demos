package com.ayad.holidaysservice.domain.service.impl;


import com.ayad.holidaysservice.clients.proxy.ifc.HolidayApiClient;
import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test suite for {@link HolidaysServiceImplTest}.
 * Tests service layer in isolation with mocked proxy client layer.
 * Verifies logic handling and parameter validation.
 */
@ExtendWith(MockitoExtension.class)
class HolidaysServiceImplTest {

    @Mock
    private HolidayApiClient apiClient;
    @Mock
    private ExecutorService executor;
    @InjectMocks
    private HolidaysServiceImpl service;


    // Successful non-weekend count calculation
    @Test
    void getNonWeekendHolidayCount_Success() {
        when(apiClient.getPublicHolidays("US", 2025))
                .thenReturn(
                        List.of(new PublicHoliday(
                                LocalDate.of(2025, 1, 2),
                                "testName",
                                "testLocalName")));

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        Map<String, Long> result = service.getNonWeekendHolidayCount(2025, List.of("US"));

        assertEquals(1L, result.get("US"));
    }

    // Common holidays with date overlap
    @Test
    void getCommonHolidays_WithOverlap() {
        PublicHoliday commonHoliday = new PublicHoliday(
                LocalDate.of(2025, 1, 1),
                "testName", "testLocalName");
        when(apiClient.getPublicHolidays("US", 2025)).thenReturn(List.of(commonHoliday));
        when(apiClient.getPublicHolidays("CA", 2025)).thenReturn(List.of(commonHoliday));
        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));
        List<PublicHoliday> result = service.getCommonHolidays(2025, "US", "CA");

        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2025, 1, 1), result.get(0).getDate());
    }

    // All invalid country codes
    @Test
    void getNonWeekendHolidayCount_AllInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.getNonWeekendHolidayCount(2025, List.of("INVALID1", "INVALID2"));
        });
    }
}
