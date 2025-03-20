package com.ayad.holidaysservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration class for setting up a thread pool executor for asynchronous tasks.
 * This class utilizes {@link AsyncExecutorProperties} to configure the thread pool.
 */
@Configuration
public class ThreadPoolExecutorConfig {
    private final AsyncExecutorProperties asyncExecutorProperties;

    public ThreadPoolExecutorConfig(AsyncExecutorProperties asyncExecutorProperties) {
        this.asyncExecutorProperties = asyncExecutorProperties;
    }


    /**
     * Creates and configures an {@link ExecutorService} bean for asynchronous task execution.
     *
     * @return A {@link ThreadPoolExecutor} instance configured with properties from {@code asyncExecutorProperties}.
     */
    @Bean(name = "asyncExecutor", destroyMethod = "shutdown")
    public ExecutorService asyncExecutor() {
        return new ThreadPoolExecutor(
                asyncExecutorProperties.getCorePoolSize(),
                asyncExecutorProperties.getMaxPoolSize(),
                asyncExecutorProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(asyncExecutorProperties.getQueueCapacity()),
                // Custom ThreadFactory with name prefix
                new ThreadFactory() {
                    private final AtomicInteger threadCount = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(
                                r,
                                asyncExecutorProperties.getThreadNamePrefix() + threadCount.getAndIncrement()
                        );
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}
