package com.flash.framework.core.support.event;


/**
 * 事件监听处理器
 *
 * @author zhurg
 * @date 2019/3/26 - 下午6:04
 */
public interface EventHandler<E extends BaseEvent> {

    /**
     * 事件处理方法
     *
     * @param event
     */
    void handler(E event);

}