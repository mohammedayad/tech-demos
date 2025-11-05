package com.ayad.citysorter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the asynchronous executor.
 * These properties can be set in the application's configuration file
 * (e.g., application.properties or application.yml) using the prefix "async-executor".
 */
@Data
@Component
@ConfigurationProperties(prefix = "async-executor")
public class AsyncExecutorProperties {

    private int corePoolSize = 7;          // Default: 7 threads
    private int maxPoolSize = 42;          // Default: 42 threads
    private int queueCapacity = 11;       // Default: 11 tasks
    private long keepAliveTime = 60L;      // Default: 60 seconds
    private String threadNamePrefix = "Async-Executor-";
}
