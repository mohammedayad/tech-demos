package com.ayad.holidaysservice.domain.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * Represents a public holiday with date and naming information.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PublicHoliday {

    private LocalDate date;
    private String name;
    private String localName;
}
