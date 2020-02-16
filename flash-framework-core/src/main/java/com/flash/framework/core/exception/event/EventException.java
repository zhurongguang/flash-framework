package com.flash.framework.core.exception.event;

/**
 * 事件异常类
 *
 * @author zhurg
 * @date 2019/11/26 - 下午1:53
 */
public class EventException extends RuntimeException {

    private static final long serialVersionUID = 6665002011479193161L;

    public EventException() {
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }

    public EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
