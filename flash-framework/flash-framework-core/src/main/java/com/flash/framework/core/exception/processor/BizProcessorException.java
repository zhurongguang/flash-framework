package com.flash.framework.core.exception.processor;

/**
 * 业务处理器异常
 *
 * @author zhurg
 * @date 2019/8/23 - 上午9:26
 */
public class BizProcessorException extends RuntimeException {

    private static final long serialVersionUID = -7446467249130251317L;

    public BizProcessorException() {
    }

    public BizProcessorException(String message) {
        super(message);
    }

    public BizProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizProcessorException(Throwable cause) {
        super(cause);
    }

    public BizProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}