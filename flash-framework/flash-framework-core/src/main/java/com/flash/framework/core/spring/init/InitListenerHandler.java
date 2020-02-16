package com.flash.framework.core.spring.init;

import org.springframework.context.ApplicationContext;

/**
 * Spring初始化处理器
 *
 * @author zhurg
 * @date 2019/2/1 - 下午4:34
 */
public interface InitListenerHandler {

    /**
     * 处理Spring容器初始化后的一些业务
     */
    void handle(ApplicationContext applicationContext);
}