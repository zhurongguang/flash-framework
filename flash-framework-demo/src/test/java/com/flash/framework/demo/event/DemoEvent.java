package com.flash.framework.demo.event;

import com.flash.framework.core.support.event.BaseEvent;
import com.flash.framework.core.support.event.EventSource;
import lombok.Data;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午3:05
 */
@Data
public class DemoEvent extends BaseEvent {

    private String demo;

    public DemoEvent(String eventType, EventSource source) {
        super(eventType, source);
    }
}
