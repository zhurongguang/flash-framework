package com.flash.framework.core.spring.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Spring容器初始化后执行一些业务初始化操作，初始化失败不影响启动
 *
 * @author zhurg
 * @date 2019/2/1 - 下午4:32
 */
@Slf4j
public class SpringBootInitListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static AtomicBoolean INIT = new AtomicBoolean(false);

    private List<InitListenerHandler> handlers;

    private ApplicationContext applicationContext;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @PostConstruct
    public void init() {
        Map<String, InitListenerHandler> initListenerHandlerMap = applicationContext.getBeansOfType(InitListenerHandler.class);
        if (MapUtils.isNotEmpty(initListenerHandlerMap)) {
            handlers = initListenerHandlerMap.values().stream().sorted((h1, h2) -> {
                SpringBootInitHandler springBootInitHandler1 = AnnotationUtils.findAnnotation(h1.getClass(), SpringBootInitHandler.class);
                int sort1 = Objects.nonNull(springBootInitHandler1) ? springBootInitHandler1.order() : 0;
                SpringBootInitHandler springBootInitHandler2 = AnnotationUtils.findAnnotation(h1.getClass(), SpringBootInitHandler.class);
                int sort2 = Objects.nonNull(springBootInitHandler2) ? springBootInitHandler2.order() : 0;
                return sort1 - sort2;
            }).collect(Collectors.toList());

            if (handlers.stream().filter(handler -> {
                SpringBootInitHandler anno = AnnotationUtils.findAnnotation(handler.getClass(), SpringBootInitHandler.class);
                return anno.async();
            }).findFirst().isPresent()) {
                threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
                threadPoolTaskExecutor.setCorePoolSize(1);
                threadPoolTaskExecutor.setMaxPoolSize(10);
                threadPoolTaskExecutor.setKeepAliveSeconds(3);
                threadPoolTaskExecutor.setQueueCapacity(100);
                threadPoolTaskExecutor.setThreadNamePrefix("SpringBootInitListener-thread");
                threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!INIT.getAndSet(true)) {
            if (CollectionUtils.isNotEmpty(handlers)) {
                handlers.forEach(handler -> {
                    try {
                        SpringBootInitHandler anno = AnnotationUtils.findAnnotation(handler.getClass(), SpringBootInitHandler.class);
                        if (anno.async()) {
                            threadPoolTaskExecutor.submit(() -> handler.handle(applicationContext));
                        } else {
                            handler.handle(applicationContext);
                        }
                    } catch (Throwable e) {
                        log.error("[Flash Framework] InitListenerHandler {} handle failed ,cause:", handler.getClass().getCanonicalName(), e);
                    }
                });
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}