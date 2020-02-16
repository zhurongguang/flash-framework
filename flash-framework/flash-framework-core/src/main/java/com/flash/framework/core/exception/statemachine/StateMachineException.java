package com.flash.framework.core.exception.statemachine;

/**
 * 状态机异常
 *
 * @author zhurg
 * @date 2020/2/4 - 上午9:07
 */
public class StateMachineException extends RuntimeException {

    private static final long serialVersionUID = 2609259416952558474L;

    public StateMachineException() {
    }

    public StateMachineException(String message) {
        super(message);
    }

    public StateMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateMachineException(Throwable cause) {
        super(cause);
    }

    public StateMachineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
