package com.flash.framework.core.support.fsm;

import com.flash.framework.core.exception.statemachine.StateMachineException;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * 状态机
 *
 * @author zhurg
 * @date 2020/2/3 - 下午5:59
 */
public class StateMachine<STATE, EVENT, CONTEXT> {

    private final Table<STATE, EVENT, STATE> transitions;

    private final Table<STATE, EVENT, StateMachineAction<STATE, EVENT, CONTEXT>> actions;

    @Getter
    private final String name;

    public StateMachine(String name, Table<STATE, EVENT, STATE> transitions, Table<STATE, EVENT, StateMachineAction<STATE, EVENT, CONTEXT>> actions) {
        this.name = name;
        this.transitions = HashBasedTable.create(transitions);
        this.actions = HashBasedTable.create(actions);
    }

    /**
     * 执行状态流转
     *
     * @param from
     * @param on
     * @param context
     * @return
     */
    public STATE fire(STATE from, EVENT on, CONTEXT context) {
        if (transitions.contains(from, on)) {
            STATE to = transitions.get(from, on);
            StateMachineAction action = actions.get(from, on);
            if (Objects.nonNull(action)) {
                action.action(on, to, context);
            }
            return to;
        } else {
            throw new StateMachineException(MessageFormat.format("[Flash Framework] stateMachine {0} can not find transition from {1} on {2}", name, from, on));
        }
    }


    public static class StateMachineBuilder<STATE, EVENT, CONTEXT> {

        private Table<STATE, EVENT, STATE> transitions = HashBasedTable.create();

        private Table<STATE, EVENT, StateMachineAction<STATE, EVENT, CONTEXT>> actions = HashBasedTable.create();

        private final String name;

        private StateMachineBuilder(String name) {
            this.name = name;
        }

        public static StateMachineBuilder builder(String name) {
            return new StateMachineBuilder<>(name);
        }

        /**
         * 添加transition
         *
         * @param from
         * @param on
         * @param to
         */
        public StateMachineBuilder addTransition(STATE from, EVENT on, STATE to) {
            addTransition(from, on, to, null);
            return this;
        }

        /**
         * 添加transition
         *
         * @param from
         * @param on
         * @param to
         * @param action
         */
        public StateMachineBuilder addTransition(STATE from, EVENT on, STATE to, StateMachineAction action) {
            if (transitions.contains(from, on)) {
                throw new StateMachineException(MessageFormat.format("[Flash Framework] stateMachine {0} transition from {1} on {2} to {3} already exists", from, on, to));
            }
            transitions.put(from, on, to);

            if (Objects.nonNull(action)) {
                actions.put(from, on, action);
            }
            return this;
        }

        public StateMachine<STATE, EVENT, CONTEXT> build() {
            return new StateMachine<>(this.name, this.transitions, this.actions);
        }
    }
}