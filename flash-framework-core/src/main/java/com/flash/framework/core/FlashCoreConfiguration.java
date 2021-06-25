package com.flash.framework.core;

import com.flash.framework.core.spring.context.ApplicationContextHolder;
import com.flash.framework.core.spring.init.SpringBootInitListener;
import com.flash.framework.core.support.event.log.EventLogAop;
import com.flash.framework.core.support.event.log.EventLogHandler;
import com.flash.framework.core.support.event.log.RedisEventLogHandler;
import com.flash.framework.core.support.processor.BizProcessorManager;
import com.flash.framework.core.support.retry.RetryAspect;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhurg
 * @date 2019/2/1 - 下午3:53
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.flash.framework.core"})
public class FlashCoreConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public BizProcessorManager bizProcessorManager() {
        return new BizProcessorManager();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBootInitListener springBootInitListener() {
        return new SpringBootInitListener();
    }

    @Bean
    @ConditionalOnMissingBean(name = "bizProcessorTaskExecutor")
    public ThreadPoolTaskExecutor bizProcessorTaskExecutor(@Value("${flash.framework.bizProcessors.queue:1024}") int queue,
                                                           @Value("${flash.framework.bizProcessors.keepAliveSeconds:3}") int keepAliveSeconds,
                                                           @Value("${flash.framework.bizProcessors.corePoolSize:10}") int corePoolSize,
                                                           @Value("${flash.framework.bizProcessors.maxPoolSize:50}") int maxPoolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queue);
        executor.setThreadNamePrefix("BizProcessor-thread-execute");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Configuration
    @ConditionalOnProperty(name = "flash.event.enable", havingValue = "true")
    public static class EventConfiguration {

        @Bean
        public AsyncEventBus asyncEventBus(@Qualifier("eventBusThreadPoolTaskExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor) {
            return new AsyncEventBus(threadPoolTaskExecutor);
        }

        @Bean
        @ConditionalOnMissingBean(name = "eventBusThreadPoolTaskExecutor")
        public ThreadPoolTaskExecutor eventBusThreadPoolTaskExecutor(@Value("${flash.framework.eventbus.queue:1024}") int queue,
                                                                     @Value("${flash.framework.eventbus.keepAliveSeconds:3}") int keepAliveSeconds,
                                                                     @Value("${flash.framework.eventbus.corePoolSize:10}") int corePoolSize,
                                                                     @Value("${flash.framework.eventbus.maxPoolSize:50}") int maxPoolSize) {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(corePoolSize);
            executor.setMaxPoolSize(maxPoolSize);
            executor.setKeepAliveSeconds(keepAliveSeconds);
            executor.setQueueCapacity(queue);
            executor.setThreadNamePrefix("EventBus-thread-execute");
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            return executor;
        }

        @Bean
        @ConditionalOnProperty(name = "flash.framework.event.log.enable", havingValue = "true")
        @ConditionalOnMissingBean(EventLogHandler.class)
        public EventLogHandler eventLogHandler() {
            return new RedisEventLogHandler();
        }

        @Bean
        @ConditionalOnProperty(name = "flash.framework.event.log.enable", havingValue = "true")
        @ConditionalOnBean(EventLogHandler.class)
        public EventLogAop eventRecordAop(EventLogHandler eventLogHandler) {
            return new EventLogAop(eventLogHandler);
        }
    }
}