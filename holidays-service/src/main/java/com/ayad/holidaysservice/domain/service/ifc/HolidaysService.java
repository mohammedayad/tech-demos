package com.ayad.holidaysservice.domain.service.ifc;


import com.ayad.holidaysservice.domain.model.dtos.PublicHoliday;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HolidaysService {


    List<PublicHoliday> getLastCelebratedHolidays(long numberOfHolidays, String countryCode);

    Map<String, Long> getNonWeekendHolidayCount(int year, List<String> countryCodes);

    List<PublicHoliday> getCommonHolidays(int year, String countryCode1, String countryCode2);

}
