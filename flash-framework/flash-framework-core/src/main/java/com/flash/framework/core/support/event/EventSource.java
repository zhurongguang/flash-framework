package com.flash.framework.core.support.event;

import lombok.Getter;

/**
 * 事件源
 *
 * @author zhurg
 * @date 2020/2/11 - 2:31 PM
 */
public enum EventSource {

    /**
     * Spring 事件机制
     */
    SPRING("spring"),
    /**
     * EventBus事件机制
     */
    EVENT_BUS("eventBus");

    @Getter
    private String source;

    EventSource(String source) {
        this.source = source;
    }
}