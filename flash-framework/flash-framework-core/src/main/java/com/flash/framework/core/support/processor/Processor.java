package com.flash.framework.core.support.processor;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zhurg
 * @date 2019/4/26 - 下午2:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Processor {

    /**
     * processor名称
     *
     * @return
     */
    String name();

    /**
     * 条件，仅用于链式处理
     *
     * @return
     */
    String condition() default "";
}