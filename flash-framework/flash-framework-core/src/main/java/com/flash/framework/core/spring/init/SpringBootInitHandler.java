package com.flash.framework.core.spring.init;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Spring Boot初始化处理器
 *
 * @author zhurg
 * @date 2019/8/23 - 下午4:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SpringBootInitHandler {

    /**
     * 排序值
     *
     * @return
     */
    int order() default 0;

    /**
     * 是否异步执行
     *
     * @return
     */
    boolean async() default false;
}