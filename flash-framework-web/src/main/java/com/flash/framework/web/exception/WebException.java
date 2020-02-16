package com.flash.framework.web.exception;

/**
 * Web层异常信息处理
 *
 * @author zhurg
 * @date 2019/9/6 - 上午10:11
 */
public class WebException extends RuntimeException {

    private static final long serialVersionUID = -1159921635801567025L;

    public WebException() {
    }

    public WebException(String message) {
        super(message);
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebException(Throwable cause) {
        super(cause);
    }

    public WebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
