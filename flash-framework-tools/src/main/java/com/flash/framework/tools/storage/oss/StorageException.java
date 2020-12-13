package com.flash.framework.tools.storage.oss;

/**
 * @author zhurg
 * @date 2020/7/4 - 下午12:39
 */
public class StorageException extends RuntimeException {

    private static final long serialVersionUID = -5465563356403214437L;

    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }

    public StorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}