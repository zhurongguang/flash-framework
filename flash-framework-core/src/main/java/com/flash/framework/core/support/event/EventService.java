package com.flash.framework.core.support.event;

import com.flash.framework.core.exception.event.EventException;
import com.flash.framework.core.support.event.annotation.Publisher;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

/**
 * 事件服务
 *
 * @author zhurg
 * @date 2019/11/26 - 下午1:49
 */
@Component
@ConditionalOnProperty(name = "flash.framework.event.enable", havingValue = "true")
public class EventService implements ApplicationContextAware {

    private Map<String, EventPublisher> eventPusblishers = Maps.newConcurrentMap();

    /**
     * 发布事件
     *
     * @param event
     */
    public void publishEvent(BaseEvent event) {
        eventPusblishers.get(event.getSource().getSource()).publish(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EventPublisher> publisherBeans = applicationContext.getBeansOfType(EventPublisher.class);
        if (MapUtils.isNotEmpty(publisherBeans)) {
            publisherBeans.values().forEach(publisher -> {
                Publisher anno = AnnotationUtils.findAnnotation(publisher.getClass(), Publisher.class);
                if (Objects.isNull(anno)) {
                    throw new EventException(MessageFormat.format("[Flash Framework] EventPublisher class {0} must annotated by @Publisher", publisher.getClass().getCanonicalName()));
                }
                eventPusblishers.put(anno.source().getSource(), publisher);
            });
        }

        AsyncEventBus asyncEventBus = applicationContext.getBean(AsyncEventBus.class);
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (MapUtils.isNotEmpty(eventHandlerMap)) {
            eventHandlerMap.values().forEach(asyncEventBus::register);
        }
    }
}