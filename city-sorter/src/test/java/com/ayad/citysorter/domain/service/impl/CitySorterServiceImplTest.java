package com.ayad.citysorter.domain.service.impl;


import com.ayad.citysorter.common.exceptions.CitySorterServerException;
import com.ayad.citysorter.common.utils.CitySorterUtility;
import com.ayad.citysorter.config.CitySorterConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CitySorterServiceImpl}.
 *
 * <p>These tests verify the functionality of sorting cities into groups based on different criteria,
 * handling various edge cases such as empty directories, non-existent directories, special characters,
 * duplicates, case sensitivity, and large input sizes.</p>
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class CitySorterServiceImplTest {
    @Mock
    private ExecutorService executor;

    @Mock
    private CitySorterConfig citySorterConfig;

    @TempDir
    private Path tempDir;

    private CitySorterServiceImpl citySorterService;

    @BeforeEach
    void setup() {
        citySorterService = new CitySorterServiceImpl(executor,citySorterConfig);
    }


    /**
     * Tests sorting of valid city files.
     * Ensures that grouped city names are returned in the correct order.
     */
    @SneakyThrows
    @Test
    void sortCitiesWithValidFiles_ReturnsSortedGroups() {
        // Setup test files and mock
        Path citiesDir = tempDir.resolve("cities");
        Files.createDirectories(citiesDir);
        Files.write(citiesDir.resolve("file1.txt"),
                List.of("Blaricum", "Ricumbla", "Icumblar", "Cumblari", "Umblaric"));
        Files.write(citiesDir.resolve("file2.txt"),
                List.of("Sloten", "Lotens", "Tenslo", "Enslot"));
        Files.write(citiesDir.resolve("file3.txt"),
                List.of("Opmeer", "Meerop", "Eropme", "Ropmee"));

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        when(citySorterConfig.getCitiesStorageOutputFileName()).thenReturn(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);



        // Act
        List<List<String>> result = citySorterService.sortCities(citiesDir.toString());

        // verification
        assertThat(result).isNotNull()
                .containsExactly(
                        List.of("Blaricum", "Cumblari", "Icumblar", "Ricumbla", "Umblaric"),
                        List.of("Enslot", "Lotens", "Sloten", "Tenslo"),
                        List.of("Eropme", "Meerop", "Opmeer", "Ropmee")
                );
        verify(executor, atLeastOnce()).execute(any(Runnable.class));

    }


    /**
     * Tests behavior when an empty directory is provided.
     * Ensures an exception is thrown.
     */
    @SneakyThrows
    @Test
    void sortCitiesWithEmptyDirectory_ThrowsException() {
        String folderName = "empty";
        Path emptyDir = tempDir.resolve(folderName);
        Files.createDirectories(emptyDir);


        CitySorterServerException exception = assertThrows(CitySorterServerException.class,
                () -> citySorterService.sortCities(emptyDir.toString()));


        // verify
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getErrorMessage()).contains(
                String.format(CitySorterUtility.CITIES_FOLDER_DOSE_NOT_HAVE_FILES, folderName));
    }


    /**
     * Tests behavior when a non-existent directory is provided.
     */
    @SneakyThrows
    @Test
    void sortCitiesWithNonExistentDirectory_ThrowsException() {
        String folderName = "non-existent";
        CitySorterServerException exception = assertThrows(CitySorterServerException.class,
                () -> citySorterService.sortCities(folderName));


        // verify
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getErrorMessage()).contains(
                String.format(CitySorterUtility.CITIES_FOLDER_NOT_EXIST, folderName));
    }



    /**
     * Tests sorting with special characters in city names.
     */
    @SneakyThrows
    @Test
    void sortCitiesWithSpecialCharacters_HandlesCorrectly() {
        // given
        Path citiesDir = tempDir.resolve("special");
        Files.createDirectories(citiesDir);
        Files.write(citiesDir.resolve("cities.txt"), List.of("München", "Zürich", "Québec"));

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        when(citySorterConfig.getCitiesStorageOutputFileName()).thenReturn(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);



        // act
        List<List<String>> result = citySorterService.sortCities(citiesDir.toString());

        // verify
        assertThat(result).isNotNull()
                .containsExactly(
                        List.of("München"),
                        List.of("Québec"),
                        List.of("Zürich")
                );
        verify(executor, atLeastOnce()).execute(any(Runnable.class));

    }

    /**
     * Tests sorting when duplicate city names are present.
     */
    @SneakyThrows
    @Test
    void sortCitiesWithDuplicateCities_GroupsCorrectly() {
        // given
        Path citiesDir = tempDir.resolve("duplicates");
        Files.createDirectories(citiesDir);
        Files.write(citiesDir.resolve("file.txt"), List.of("Paris", "Paris", "paris"));

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        when(citySorterConfig.getCitiesStorageOutputFileName()).thenReturn(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);


        // Act
        List<List<String>> result = citySorterService.sortCities(citiesDir.toString());

        // verify
        assertThat(result).isNotNull()
                .containsExactly(
                        List.of("Paris", "Paris", "paris")
                );
        verify(executor, atLeastOnce()).execute(any(Runnable.class));
    }


    /**
     * Tests case-insensitive sorting.
     */
    @Test
    void sortCitiesWithMixedCaseCities_SortsIgnoreCaseSensitive() throws Exception {
        // given
        Path citiesDir = tempDir.resolve("mixed-case");
        Files.createDirectories(citiesDir);
        Files.write(citiesDir.resolve("file.txt"), List.of("Berlin", "berlin", "BERLIN"));

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        when(citySorterConfig.getCitiesStorageOutputFileName()).thenReturn(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);


        // Act
        List<List<String>> result = citySorterService.sortCities(citiesDir.toString());

        // verify
        assertThat(result).isNotNull()
                .containsExactly(
                        List.of("BERLIN", "Berlin", "berlin")
                );
        verify(executor, atLeastOnce()).execute(any(Runnable.class));
    }


    /**
     * Tests sorting when have large input.
     */
    @Test
    void sortCitiesWithLargeInput_HandlesEfficiently() throws Exception {
        // given
        Path citiesDir = tempDir.resolve("large-input");
        Files.createDirectories(citiesDir);

        // Generate 1000 test cities
        List<String> cities = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            cities.add("City" + i);
        }
        Files.write(citiesDir.resolve("large.txt"), cities);

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        when(citySorterConfig.getCitiesStorageOutputFileName()).thenReturn(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);


        // Act
        List<List<String>> result = citySorterService.sortCities(citiesDir.toString());

        // verify
        assertThat(result).isNotNull();
    }


    /**
     * Tests sorting and saving into external file.
     */
    @Test
    void writeSortedGroupsIntoExternalFile_WithValidData_CreatesFile() throws Exception {
        // given
        Path citiesDir = tempDir.resolve("output-test");
        Files.createDirectories(citiesDir);
        Files.write(citiesDir.resolve("cities.txt"), List.of("Vienna", "Berlin"));

        // Mock Runnable execution (used by supplyAsync)
        doAnswer(inv -> {
            Runnable task = inv.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        when(citySorterConfig.getCitiesStorageOutputFileName()).thenReturn(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);


        // Act
        citySorterService.sortCities(citiesDir.toString());

        // Verify output file
        Path outputPath = Path.of(CitySorterUtility.CITY_SORTER_OUTPUT_FILE);
        assertThat(Files.readAllLines(outputPath))
                .containsExactly("Berlin", "Vienna");
    }


}
