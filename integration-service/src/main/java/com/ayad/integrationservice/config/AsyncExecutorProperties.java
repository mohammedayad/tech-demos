package com.ayad.integrationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the async thread pool executor.
 * These properties can be set in the application's configuration file
 * (e.g., application.properties or application.yml) using the prefix "async-executor".
 */
@Data
@Component
@ConfigurationProperties(prefix = "async-executor")
public class AsyncExecutorProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
}
