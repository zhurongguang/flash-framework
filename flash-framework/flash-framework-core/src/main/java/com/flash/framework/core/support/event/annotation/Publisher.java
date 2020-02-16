package com.flash.framework.core.support.event.annotation;

import com.flash.framework.core.support.event.EventSource;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午1:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Publisher {

    /**
     * 事件来源
     *
     * @return
     */
    EventSource source();
}