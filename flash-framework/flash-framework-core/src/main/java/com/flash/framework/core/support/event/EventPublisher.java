package com.flash.framework.core.support.event;

/**
 * 事件发布
 *
 * @author zhurg
 * @date 2019/11/26 - 下午1:37
 */
public interface EventPublisher {

    /**
     * 发送事件
     *
     * @param event
     */
    void publish(BaseEvent event);
}