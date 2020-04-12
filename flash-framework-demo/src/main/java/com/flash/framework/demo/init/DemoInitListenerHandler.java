package com.flash.framework.demo.init;

import com.flash.framework.core.spring.init.InitListenerHandler;
import com.flash.framework.core.spring.init.SpringBootInitHandler;
import org.springframework.context.ApplicationContext;

/**
 * @author zhurg
 * @date 2020/3/1 - 9:26 PM
 */
@SpringBootInitHandler(order = 1)
public class DemoInitListenerHandler implements InitListenerHandler {

    @Override
    public void handle(ApplicationContext applicationContext) {
        System.out.println("DemoInitListenerHandler do handle");
    }
}
