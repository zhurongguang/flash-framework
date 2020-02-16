package com.flash.framework.core.exception.extension;

/**
 * 扩展点异常
 *
 * @author zhurg
 * @date 2020/1/3 - 下午3:37
 */
public class ExtensionException extends RuntimeException {

    private static final long serialVersionUID = -6279915317134444940L;

    public ExtensionException() {
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }

    public ExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}