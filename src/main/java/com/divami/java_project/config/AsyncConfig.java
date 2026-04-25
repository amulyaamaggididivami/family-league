package com.divami.java_project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configures the thread pool used by {@code @Async} methods.
 * Scoring and leaderboard recalculation run on this pool after result publication.
 */
@Configuration
public class AsyncConfig {

    @Value("${familyleague.async.core-pool-size:4}")
    private int corePoolSize;

    @Value("${familyleague.async.max-pool-size:8}")
    private int maxPoolSize;

    @Value("${familyleague.async.queue-capacity:100}")
    private int queueCapacity;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("fl-async-");
        executor.initialize();
        return executor;
    }
}
