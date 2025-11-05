package com.ayad.citysorter.common.utils;


import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * Utility class providing helper methods and constants for the City Sorter service.
 */
@UtilityClass
public class CitySorterUtility {

    public final String CITIES_STORAGE_FOLDER = "cities-storage";  // Folder inside resources
    public final String CITY_SORTER_OUTPUT_FILE = "city-sorter-output.txt";

    public final String CITIES_STORAGE_ERROR = "CITIES_STORAGE_ERROR";

    public final String EMPTY_FILES_ERROR = "EMPTY_FILES_ERROR";

    public final String EMPTY_FILES_ERROR_MESSAGE = "All city files are empty - no data to process";

    public final String GROUPING_CITIES_ERROR = "GROUPING_CITIES_ERROR";
    public final String READ_CITIES_ERROR_MESSAGE = "Failed to read cities from cities folder: Error: %s";
    public final String READ_CITIES_FRM_FILE_ERROR_MESSAGE = "Failed to read cities from file: %s Error: %s";
    public final String GROUPING_CITIES_ERROR_MESSAGE = "Failed to group cities: Error: %s";
    public static final String STORE_SORTED_CITIES_ERROR_MESSAGE = "Failed to store sorted cities output file. Error: %s";

    public final String CITIES_FOLDER_NOT_EXIST = "%s folder not exist";

    public final String CITIES_FOLDER_DOSE_NOT_HAVE_FILES = "%s folder does not have files";
    public final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server error";
    public final String SERVER_ERROR_MESSAGE = "Server error";

    public final String CLASS_PATH_PREFIX = "classpath:";


    /**
     * Generates a sorted key from a given city name.
     * This method sorts the characters in the city name alphabetically,
     * providing a consistent key for grouping similar city names.
     *
     * @param city The city name to be processed.
     * @return A sorted key representation of the city name.
     * @throws IllegalArgumentException if the input city name is null or empty.
     */
    public String sortedKey(String city) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty");
        }
        char[] chars = city.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }


    /**
     * Checks if the given directory contains any files.
     *
     * @param directory The directory to check.
     * @return {@code true} if the directory contains at least one file, {@code false} otherwise.
     * @throws IOException if an I/O error occurs while accessing the directory.
     */
    public boolean hasFiles(Path directory) throws IOException {
        try (Stream<Path> paths = Files.find(
                directory, // Start directory
                Integer.MAX_VALUE, // Recursive depth (all subdirectories)
                (path, attrs) -> attrs.isRegularFile() // Check if it's a regular file
        )) {
            return paths.findAny().isPresent(); // True if at least one file exists
        }
    }

}
