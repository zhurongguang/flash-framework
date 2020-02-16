package com.flash.framework.core.support.event.eventbus;

import com.flash.framework.core.support.event.BaseEvent;
import com.flash.framework.core.support.event.EventPublisher;
import com.flash.framework.core.support.event.EventSource;
import com.flash.framework.core.support.event.annotation.Publisher;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * EventBus 事件处理实现
 *
 * @author zhurg
 * @date 2019/11/26 - 下午1:45
 */
@Publisher(source = EventSource.EVENT_BUS)
@ConditionalOnProperty(name = "flash.event.enable", havingValue = "true")
public class EventBusEventPublisher implements EventPublisher {

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public void publish(BaseEvent event) {
        asyncEventBus.post(event);
    }
}