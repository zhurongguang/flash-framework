package com.flash.framework.core.support.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 事件基类
 *
 * @author zhurg
 * @date 2019/11/26 - 上午11:14
 */
@Data
public abstract class BaseEvent implements Serializable {

    private static final long serialVersionUID = 2108265527544194047L;

    public BaseEvent(String eventType, EventSource source) {
        this.eventType = eventType;
        this.source = source;
    }

    /**
     * 事件id
     */
    private String eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件来源
     */
    private EventSource source;
}