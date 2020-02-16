package com.flash.framework.core.support.extension.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展实现引用注解
 *
 * @author zhurg
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Extension {

    /**
     * 与@ExtensionProvider 中的group对应
     *
     * @return 扩展点组名
     */
    String value();
}
