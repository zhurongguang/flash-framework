package com.flash.framework.core.exception;

/**
 * @author zhurg
 * @date 2020/6/20 - 下午4:33
 */
public class FlashFrameworkException extends RuntimeException {

    private static final long serialVersionUID = -3279353438572673099L;

    public FlashFrameworkException() {
    }

    public FlashFrameworkException(String message) {
        super(message);
    }

    public FlashFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlashFrameworkException(Throwable cause) {
        super(cause);
    }

    public FlashFrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}