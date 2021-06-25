package com.flash.framework.core.exception.sequence;

import com.flash.framework.core.exception.FlashFrameworkException;

/**
 * @author zhurg
 * @date 2021/6/25 - 上午10:44
 */
public class SequenceException extends FlashFrameworkException {

    private static final long serialVersionUID = 6776641499155368954L;

    public SequenceException() {
    }

    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SequenceException(Throwable cause) {
        super(cause);
    }

    public SequenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}