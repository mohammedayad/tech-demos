package com.ayad.citysorter.domain.service.ifc;

import java.util.List;


/**
 * Service interface for sorting cities.
 */
public interface CitySorterService {

    /**
     * Sorts cities from the specified folder.
     *
     * @param citiesFolderName The name of the folder containing city data.
     * @return A list of grouped and sorted city names.
     */
    List<List<String>> sortCities(String citiesFolderName);
}
