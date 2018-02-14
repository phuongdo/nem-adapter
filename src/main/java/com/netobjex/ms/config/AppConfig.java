package com.netobjex.ms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AppConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

    @Bean(name = "processExecutor")
    public TaskExecutor workExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(Integer.parseInt(EnvConfig.getEnv().getProperty("threadPool.corePoolSize")));
        threadPoolTaskExecutor.setMaxPoolSize(Integer.parseInt(EnvConfig.getEnv().getProperty("threadPool.maxPoolSize")));
        threadPoolTaskExecutor.setQueueCapacity(Integer.parseInt(EnvConfig.getEnv().getProperty("threadPool.queueCapacity")));
        threadPoolTaskExecutor.afterPropertiesSet();
        LOG.info("ThreadPoolTaskExecutor set");
        return threadPoolTaskExecutor;
    }

}
