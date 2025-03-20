package com.ayad.holidaysservice.clients.proxy.ifc;

import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;

import java.util.List;

public interface HolidayApiClient {

    List<PublicHoliday> getPublicHolidays(String countryCode, int year);
}
