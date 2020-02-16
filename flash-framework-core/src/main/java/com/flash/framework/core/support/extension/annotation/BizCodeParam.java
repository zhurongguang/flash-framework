package com.flash.framework.core.support.extension.annotation;

import java.lang.annotation.*;

/**
 * bizCode 参数标识
 *
 * @author zhurg
 */
@Target(value = {ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BizCodeParam {

    /**
     * spel 表达式
     *
     * @return
     */
    String el() default "";
}