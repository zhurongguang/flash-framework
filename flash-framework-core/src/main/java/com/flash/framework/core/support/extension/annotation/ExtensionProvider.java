package com.flash.framework.core.support.extension.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点实现注解
 *
 * @author zhurg
 * @date 2020/1/3 - 下午2:56
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ExtensionProvider {

    /**
     * 扩展实现分组, 与@Extension 中的value对应
     *
     * @return
     */
    String group();

    /**
     * 全局唯一业务编码
     *
     * @return
     */
    String[] bizCode();

    /**
     * 扩展实现描述
     *
     * @return
     */
    String desc() default "";

    /**
     * 是否为默认实现，若当前执行bizCode的实现不存在，则走默认实现
     *
     * @return
     */
    boolean isDefault() default false;
}