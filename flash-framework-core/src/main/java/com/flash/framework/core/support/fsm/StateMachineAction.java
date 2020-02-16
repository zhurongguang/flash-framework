package com.flash.framework.core.support.fsm;

/**
 * Action
 *
 * @author zhurg
 * @date 2020/2/4 - 上午9:19
 */
public interface StateMachineAction<STATE, EVENT, CONTEXT> {

    /**
     * action 方法
     *
     * @param event
     * @param to
     * @param context
     */
    void action(EVENT event, STATE to, CONTEXT context);
}