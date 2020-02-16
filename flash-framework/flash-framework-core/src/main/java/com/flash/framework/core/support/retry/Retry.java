package com.flash.framework.core.support.retry;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 方法调用重试注解
 *
 * @author zhurg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {

    /**
     * 重试次数
     *
     * @return
     */
    int retry() default 1;

    /**
     * 重试间隔时间
     *
     * @return
     */
    long interval() default 500;

    /**
     * 触发重试的异常
     *
     * @return
     */
    Class<? extends Throwable> exception() default Exception.class;

    /**
     * @return
     */
    TimeUnit time() default TimeUnit.MILLISECONDS;
}