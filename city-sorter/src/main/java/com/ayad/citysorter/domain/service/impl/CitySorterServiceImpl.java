package com.ayad.citysorter.domain.service.impl;

import com.ayad.citysorter.common.exceptions.CitySorterServerException;
import com.ayad.citysorter.common.utils.CitySorterUtility;
import com.ayad.citysorter.config.CitySorterConfig;
import com.ayad.citysorter.domain.service.ifc.CitySorterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * Service implementation for sorting city names from files.
 * This service reads city names from files in a specified folder, groups them based on sorting criteria,
 * sorts them, and writes the sorted groups into an output file. The sorting process is executed asynchronously
 * using an {@link ExecutorService}.
 */

@Slf4j
@Service
public class CitySorterServiceImpl implements CitySorterService {

    private final ExecutorService executor;
    private final CitySorterConfig citySorterConfig;


    /**
     * Constructs a new CitySorterServiceImpl.
     *
     * @param executor         the executor service used for asynchronous processing
     * @param citySorterConfig the configuration for city sorter storage and processing
     */

    public CitySorterServiceImpl(@Qualifier("asyncExecutor") ExecutorService executor,
                                 CitySorterConfig citySorterConfig) {
        this.executor = executor;
        this.citySorterConfig = citySorterConfig;
    }


    /**
     * Sorts cities from the given folder, grouping and writing them into an output file.
     *
     * @param citiesFolderName the folder containing city name files
     * @return a list of sorted city groups
     */
    @Override
    public List<List<String>> sortCities(String citiesFolderName) {
        long startTime = System.currentTimeMillis();
        log.info("start sorting the cities from {} ....", citiesFolderName);
        // Step 1: Load files
        long loadStart = System.currentTimeMillis();
        List<List<String>> filesFutures = loadFilesAndReadCitiesNames(citiesFolderName);
        log.info("cities have been read from {} files (took {} ms)", filesFutures.size(),
                System.currentTimeMillis() - loadStart);

        // Step 2: Group cities
        long groupStart = System.currentTimeMillis();
        log.info("start grouping the cities....");
        Map<String, List<String>> groupedCities = groupCities(filesFutures);
        log.info("Grouped cities into {} groups (took {} ms)",
                groupedCities.size(),
                System.currentTimeMillis() - groupStart);

        // Step 3: Sort groups
        long sortStart = System.currentTimeMillis();
        log.info("start sorting cities groups....");
        List<List<String>> sortedCitiesGroups = sortCitiesGroups(groupedCities);
        log.info("Sorted {} city groups (took {} ms)",
                sortedCitiesGroups.size(),
                System.currentTimeMillis() - sortStart);

        // Step 4: Write to file
        long writeStart = System.currentTimeMillis();
        log.info("start saving sorted grouped cities into external file");
        writeSortedGroupsIntoExternalFile(sortedCitiesGroups);
        log.info("cities sorted and saved into {} successfully (took {} ms)", citySorterConfig.getCitiesStorageOutputFileName()
                , System.currentTimeMillis() - writeStart);

        // Total time
        long totalTime = System.currentTimeMillis() - startTime;
        log.info("END: City sorting process completed in {} ms", totalTime);
        return sortedCitiesGroups;
    }


    /**
     * Loads files from the given folder and reads city names.
     *
     * @param folderName the folder containing city name files
     * @return a list of city name lists, where each list represents a file
     */
    private List<List<String>> loadFilesAndReadCitiesNames(String folderName) {
        try {
            log.debug("start loading cities from external folder....");
            Path storageDirectory;
            if (folderName.startsWith(CitySorterUtility.CLASS_PATH_PREFIX)) {
                // Handle classpath resources
                String resourcePath = folderName.substring(CitySorterUtility.CLASS_PATH_PREFIX.length());
                ClassPathResource resource = new ClassPathResource(resourcePath);
                storageDirectory = Paths.get(resource.getURI());
            } else {
                // Handle filesystem paths
                storageDirectory = Paths.get(folderName);
            }

            // check the directory exist
            if (!Files.exists(storageDirectory) || !Files.isDirectory(storageDirectory)) {
                String errorMessage = String.format(CitySorterUtility.CITIES_FOLDER_NOT_EXIST, storageDirectory.getFileName());
                log.error(errorMessage);
                throw new CitySorterServerException(HttpStatus.INTERNAL_SERVER_ERROR,
                        CitySorterUtility.CITIES_STORAGE_ERROR,
                        String.format(CitySorterUtility.READ_CITIES_ERROR_MESSAGE,
                                errorMessage));
            }
            if (!CitySorterUtility.hasFiles(storageDirectory)) {
                String errorMessage = String.format(CitySorterUtility.CITIES_FOLDER_DOSE_NOT_HAVE_FILES, storageDirectory.getFileName());
                log.error(errorMessage);
                throw new CitySorterServerException(HttpStatus.INTERNAL_SERVER_ERROR,
                        CitySorterUtility.CITIES_STORAGE_ERROR,
                        String.format(CitySorterUtility.READ_CITIES_ERROR_MESSAGE,
                                errorMessage));

            }
            try (Stream<Path> paths = Files.walk(storageDirectory)) {
                List<CompletableFuture<List<String>>> filesFutures = paths
                        .filter(Files::isRegularFile)
                        .map(file -> CompletableFuture.supplyAsync(() -> readCityNames(file), executor))
                        .toList();

                CompletableFuture.allOf(filesFutures.toArray(new CompletableFuture[0])).join();

                List<List<String>> cityLists = filesFutures.stream()
                        .map(CompletableFuture::join)
                        .toList();

                // Check if all files are empty
                if (cityLists.stream().allMatch(List::isEmpty)) {
                    log.error(CitySorterUtility.EMPTY_FILES_ERROR_MESSAGE);
                    throw new CitySorterServerException(
                            HttpStatus.BAD_REQUEST,
                            CitySorterUtility.EMPTY_FILES_ERROR,
                            String.format(CitySorterUtility.READ_CITIES_ERROR_MESSAGE, CitySorterUtility.EMPTY_FILES_ERROR_MESSAGE)
                    );
                }
                return cityLists;
            }
        } catch (IOException e) {
            log.error("Error loading cities from cities-storage folder: ", e);
            throw new CitySorterServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    CitySorterUtility.CITIES_STORAGE_ERROR,
                    String.format(CitySorterUtility.READ_CITIES_ERROR_MESSAGE, e.getMessage())
            );

        }


    }

    /**
     * Reads city names from a given file.
     *
     * @param file the file containing city names
     * @return a list of city names
     */
    private List<String> readCityNames(Path file) {
        try {
            log.debug("start read cities names from file {}", file.getFileName());
            return Files.readAllLines(file)
                    .stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();
        } catch (IOException e) {
            log.error("Error reading cities from file {} ", file.getFileName(), e);  // Error logging
            throw new CitySorterServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    CitySorterUtility.CITIES_STORAGE_ERROR,
                    String.format(CitySorterUtility.READ_CITIES_FRM_FILE_ERROR_MESSAGE, file.getFileName(), e.getMessage())  // Custom Message
            );
        }
    }


    /**
     * Groups cities based on a sorting key.
     *
     * @param filesFutures the list of city name lists from multiple files
     * @return a map where the key represents a sorting group, and the value is a list of cities in that group
     */
    private Map<String, List<String>> groupCities(List<List<String>> filesFutures) {
        log.debug("start grouping the cities from {} files", filesFutures.size());
        Map<String, List<String>> groupedCities = new HashMap<>(filesFutures.size() * 2);
        for (List<String> cities : filesFutures) {
            for (String city : cities) {
                String key = CitySorterUtility.sortedKey(city);
                groupedCities.computeIfAbsent(key, k -> new ArrayList<>()).add(city);
            }
        }
        log.debug("grouped cities {}", groupedCities);
        return groupedCities;
    }


    /**
     * Sorts grouped city names alphabetically.
     *
     * @param groupedCities a map of grouped cities
     * @return a sorted list of city name groups
     */
    private List<List<String>> sortCitiesGroups(Map<String, List<String>> groupedCities) {
        log.debug("start sorting grouped cities {}", groupedCities);
        List<List<String>> sortedCitiesGroups = groupedCities.values().stream()
                .peek(Collections::sort) // Sort names in each group
                .sorted(Comparator.comparing(group -> group.isEmpty() ? "" : group.get(0))) // Sort groups
                .toList();
        log.debug("sorted grouped cities {}", sortedCitiesGroups);
        return sortedCitiesGroups;
    }


    /**
     * Writes sorted city groups to an external file.
     *
     * @param sortedCityGroups the sorted city groups
     */
    private void writeSortedGroupsIntoExternalFile(List<List<String>> sortedCityGroups) {
        try {
            log.debug("start store {} grouped sorted cities into {} file", sortedCityGroups.size(),
                    citySorterConfig.getCitiesStorageOutputFileName());
            List<String> lines = sortedCityGroups.stream()
                    .map(group -> String.join(" ", group))
                    .toList();
            Files.write(Paths.get(citySorterConfig.getCitiesStorageOutputFileName()), lines);
        } catch (IOException e) {
            log.error("Error saving city sorter output file to resources folder:", e);
            throw new CitySorterServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    CitySorterUtility.CITY_SORTER_OUTPUT_FILE,
                    String.format(CitySorterUtility.STORE_SORTED_CITIES_ERROR_MESSAGE, e.getMessage())
            );
        }
    }

}
