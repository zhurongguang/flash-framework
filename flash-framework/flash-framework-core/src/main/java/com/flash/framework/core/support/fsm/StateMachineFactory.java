package com.flash.framework.core.support.fsm;

import com.flash.framework.core.exception.statemachine.StateMachineException;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author zhurg
 * @date 2020/2/4 - 上午10:09
 */
public abstract class StateMachineFactory {

    private Map<String, StateMachine> stateMachines = Maps.newConcurrentMap();

    public StateMachineFactory() {
        configure();
    }

    /**
     * 配置StateMachine
     */
    public abstract void configure();

    /**
     * 添加状态机
     *
     * @param stateMachine
     */
    protected void addStateMachine(StateMachine stateMachine) {
        stateMachines.put(stateMachine.getName(), stateMachine);
    }

    /**
     * 获取状态机
     *
     * @param name
     * @return
     */
    public StateMachine get(String name) {
        if (!stateMachines.containsKey(name)) {
            throw new StateMachineException(MessageFormat.format("[Flash Framework] stateMachine {0} not exists", name));
        }
        return stateMachines.get(name);
    }
}