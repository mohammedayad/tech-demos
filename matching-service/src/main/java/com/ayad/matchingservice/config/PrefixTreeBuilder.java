package com.ayad.matchingservice.config;

import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrefixTreeBuilder implements ApplicationRunner {

    private final PrefixMatchingService prefixMatchingService;

    public PrefixTreeBuilder(PrefixMatchingService prefixMatchingService) {
        this.prefixMatchingService = prefixMatchingService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("building the tree");
        long startTime = System.nanoTime();
        prefixMatchingService.buildTrie(prefixMatchingService.getAllPrefixes());
        log.info("tree has been built");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  // Calculate the duration in nanoseconds
        double durationInSeconds = duration / 1_000_000_000.0;  // Convert to seconds
        log.info("Time consumed for building the tree:{} seconds",durationInSeconds);

    }
}
