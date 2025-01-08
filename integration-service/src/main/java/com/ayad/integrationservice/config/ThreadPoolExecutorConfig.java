package com.ayad.integrationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolExecutorConfig {
    private final AsyncExecutorProperties asyncExecutorProperties;

    public ThreadPoolExecutorConfig(AsyncExecutorProperties asyncExecutorProperties) {
        this.asyncExecutorProperties = asyncExecutorProperties;
    }


    /**
     * Creates and configures a {@link ThreadPoolTaskExecutor} for handling asynchronous tasks.
     * <p>
     * The properties for the thread pool are configured using the values provided by {@link AsyncExecutorProperties}.
     * The executor is customized with the following settings:
     * </p>
     * <ul>
     *   <li>Core Pool Size: The number of threads to keep in the pool, even if they are idle.</li>
     *   <li>Max Pool Size: The maximum number of threads to allow in the pool.</li>
     *   <li>Queue Capacity: The capacity of the queue to hold tasks before they are executed.</li>
     *   <li>Thread Name Prefix: A prefix for the names of newly created threads.</li>
     * </ul>
     * <p>
     * This bean is named "asyncExecutor" and can be used with the {@link org.springframework.scheduling.annotation.Async}
     * annotation to execute methods asynchronously.
     *
     * @return a configured {@link ThreadPoolTaskExecutor} instance
     */
    @Bean(name = "asyncExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(asyncExecutorProperties.getCorePoolSize());
        taskExecutor.setMaxPoolSize(asyncExecutorProperties.getMaxPoolSize());
        taskExecutor.setQueueCapacity(asyncExecutorProperties.getQueueCapacity());
        taskExecutor.setThreadNamePrefix("Async-Executor-");
        return taskExecutor;
    }

}
