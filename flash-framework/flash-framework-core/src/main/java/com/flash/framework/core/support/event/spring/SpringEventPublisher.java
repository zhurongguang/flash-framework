package com.flash.framework.core.support.event.spring;

import com.flash.framework.core.support.event.BaseEvent;
import com.flash.framework.core.support.event.EventPublisher;
import com.flash.framework.core.support.event.EventSource;
import com.flash.framework.core.support.event.annotation.Publisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Spring 事件实现
 *
 * @author zhurg
 * @date 2019/11/26 - 下午1:41
 */
@Publisher(source = EventSource.SPRING)
@ConditionalOnProperty(name = "flash.event.enable", havingValue = "true")
public class SpringEventPublisher implements EventPublisher, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(BaseEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
