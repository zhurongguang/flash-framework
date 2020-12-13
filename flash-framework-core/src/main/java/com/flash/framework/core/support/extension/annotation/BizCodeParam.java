package com.flash.framework.core.support.extension.annotation;

import org.springframework.core.annotation.AliasFor;

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

    /**
     * spel 表达式
     *
     * @return
     */
    @AliasFor(attribute = "el")
    String value() default "";
}