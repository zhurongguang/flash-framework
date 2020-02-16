package com.flash.framework.core.exception.retry;


/**
 * 重试异常
 *
 * @author zhurg
 * @date 2018/10/14 - 下午9:23
 */
public class MethodRetryException extends RuntimeException {

    private static final long serialVersionUID = -8359931001502664806L;

    public MethodRetryException() {
    }

    public MethodRetryException(String message) {
        super(message);
    }

    public MethodRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodRetryException(Throwable cause) {
        super(cause);
    }

    public MethodRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}